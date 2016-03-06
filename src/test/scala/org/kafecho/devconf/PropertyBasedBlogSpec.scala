package org.kafecho.devconf

import org.scalacheck.{Arbitrary, Gen}
import org.scalatest.WordSpec
import org.scalatest.Matchers
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import org.scalatest.prop.GeneratorDrivenPropertyChecks

@RunWith(classOf[JUnitRunner])
class PropertyBasedBlogSpec extends WordSpec with Matchers with GeneratorDrivenPropertyChecks {

  implicit override val generatorDrivenConfig = PropertyCheckConfig(
    minSuccessful = 100,
    minSize=50
  )

  val strings = Arbitrary.arbitrary[String]

  "The blog" should {

    "correctly record arbitrary text" in {

      forAll(strings) { s: String =>
        println (s)
        Blog.write("random.txt",s)
        Blog.read("random.txt") should be (s)
      }
    }
  }
}