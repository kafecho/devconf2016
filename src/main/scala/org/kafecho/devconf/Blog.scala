package org.kafecho.devconf

import java.io.File

/** An awesome blog engine which is going to change the world.*/
object Blog{

  /** Create a blog post with some text.
    * @param blogName: name of the blog post on the file system
    * @param blogContent: text to save in the blog post
    */
  def write(blogName: String, blogContent: String) : Unit = {
    Utils.writeStringToFile(new File(blogName), blogContent)
  }

  /** Fetch all the text from a blog post.
    * @param blogName: name of the blog post on the file system
    * @return the content of the blog post
    */
  def read(blogName: String) : String = {
    Utils.readFileToString(new File(blogName))
  }
}
