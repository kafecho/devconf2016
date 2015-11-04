package com.quantel.qstack.couchdb

import com.sun.jersey.api.client.Client
import javax.ws.rs.core.MediaType
import com.sun.jersey.api.client.ClientResponse
import com.sun.jersey.client.apache.ApacheHttpClient
import com.sun.jersey.api.client.config.ClientConfig
import com.sun.jersey.api.client.config.DefaultClientConfig
import scala.concurrent.duration._
import com.sun.jersey.api.client.filter.LoggingFilter
import spray.json.DefaultJsonProtocol
import spray.json._

import javax.ws.rs.ext.MessageBodyReader
import javax.ws.rs.ext.MessageBodyWriter
import java.lang.annotation.Annotation
import java.lang.reflect.Type
import javax.ws.rs.core.MediaType
import javax.ws.rs.core.MultivaluedMap
import javax.ws.rs.ext.Provider
import javax.ws.rs.Consumes
import javax.ws.rs.Produces
import java.io.InputStream
import java.io.OutputStream

import com.sun.jersey.core.util.ReaderWriter
case class Item(field: Int, _rev: Option[String] = None)

case class Response(ok: Boolean, id: String, rev: String)

object MyJsonProtocol extends DefaultJsonProtocol {
  implicit val itemFormat = jsonFormat2(Item)
  implicit val responseFormat = jsonFormat3(Response)
}

import MyJsonProtocol._

abstract class GenericJSONMessageBodyReader[T](implicit val tag: reflect.ClassTag[T], implicit val reader: JsonReader[T])
  extends MessageBodyReader[T] {

  def isReadable(aClass: Class[_], genericType: Type, annotations: Array[Annotation], mediaType: MediaType): Boolean =
    tag.runtimeClass.isAssignableFrom(aClass)
  def readFrom(aClass: Class[T], genericType: Type, annotations: Array[Annotation], mediaType: MediaType,
    httpHeaders: MultivaluedMap[String, String], entityStream: InputStream): T = {

    // parse the input stream to JSON.
    // convert the JSON to an instance.
    val json = JsonParser(ReaderWriter.readFromAsString(entityStream, mediaType))
    json.convertTo[T]
  }
}

abstract class GenericJSONMessageBodyWriter[T](implicit val tag: reflect.ClassTag[T], implicit val writer: JsonWriter[T])
  extends MessageBodyWriter[T] {
  def isWriteable(aClass: Class[_], aType: Type, annotations: Array[Annotation], mediaType: MediaType) = tag.runtimeClass.isAssignableFrom(aClass)
  def getSize(t: T, aClass: Class[_], aType: Type, annotations: Array[Annotation], mediaType: MediaType) = -1L
  def writeTo(t: T, aClass: Class[_], aType: Type, annotations: Array[Annotation], mediaType: MediaType,
    stringObjectMultivaluedMap: MultivaluedMap[String, Object], outputStream: OutputStream) =
    ReaderWriter.writeToAsString(t.toJson.compactPrint, outputStream, mediaType)
}

@Provider
@Consumes(Array(MediaType.APPLICATION_JSON))
object ResponseJSONMessageBodyReader extends GenericJSONMessageBodyReader[Response]

/** A message body writer that serializes an HTTPError to a JSON representation */
@Provider
@Produces(Array(MediaType.APPLICATION_JSON))
object ItemJSONMessageBodyWriter extends GenericJSONMessageBodyWriter[Item]


object Test extends App {

  val connectionTimeout = 2000

  val clientConfig = new DefaultClientConfig

  clientConfig.getProperties.put(ClientConfig.PROPERTY_CONNECT_TIMEOUT, new Integer(connectionTimeout))

  clientConfig.getSingletons.add(ResponseJSONMessageBodyReader)
  clientConfig.getSingletons.add(ItemJSONMessageBodyWriter)
  
  val client = ApacheHttpClient.create(clientConfig)

  client.addFilter(new LoggingFilter(System.out));

  val couch1 = client.resource("http://192.168.56.101:5984");
  val response = couch1.`type`(MediaType.APPLICATION_JSON).get(classOf[ClientResponse])

  val database = "test"
  val document = "myDoc"

  val delete = couch1.path(database).delete(classOf[ClientResponse])
  println(s"Deleted $delete.")
  delete.close()

  val create = couch1.path(database).put(classOf[ClientResponse])
  println(s"Create $create.")
  create.close()

  var _rev: Option[String] = None
  for (i <- 1 to 100) {
    val item = Item(i, _rev)
    val result = couch1.path(database).path(document).accept(MediaType.APPLICATION_JSON).`type`(MediaType.APPLICATION_JSON).put(classOf[ClientResponse], item)
    //val entity = response.getEntity(classOf[String]) 
    if (result.getStatus != 409) {
      val response = result.getEntity(classOf[Response])
      println(response)
      _rev = Some(response.rev)
    } else {
      result.close()
    }
  }
}