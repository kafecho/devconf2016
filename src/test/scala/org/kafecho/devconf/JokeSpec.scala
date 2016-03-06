package org.kafecho.devconf

import org.junit.runner.RunWith
import org.scalatest.Matchers
import org.scalatest.WordSpec
import org.scalatest.Matchers
import org.junit.runner.RunWith
import org.scalatest.WordSpec
import org.scalatest.junit.JUnitRunner
import org.scalatest.junit.JUnitRunner
import org.scalatest.prop.Configuration.PropertyCheckConfig
import org.scalatest.prop.GeneratorDrivenPropertyChecks
import java.nio.charset.Charset

import org.scalatest.prop.GeneratorDrivenPropertyChecks

@RunWith(classOf[JUnitRunner])
class JokeSpec extends WordSpec with Matchers with GeneratorDrivenPropertyChecks {

  implicit override val generatorDrivenConfig = PropertyCheckConfig(minSuccessful = 100, minSize=10)

  "The blog" should {

    "correctly record arbitrary jokes" in {

      forAll { (who: String, quantity: Int, what: String) =>

        val joke = s"A $who walks into a bar and order $quantity $what."
        println (joke)

        Blog.write("joke.txt",joke)
        Blog.read("joke.txt") should be (joke)
      }
    }
  }
}