package org.kafecho.devconf

import org.scalacheck.Gen
import org.scalatest.WordSpec
import org.scalatest.Matchers
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import org.scalatest.prop.GeneratorDrivenPropertyChecks

@RunWith(classOf[JUnitRunner])
class YummyFoodSpec extends WordSpec
with Matchers
with GeneratorDrivenPropertyChecks {

  val fruits   = Gen.oneOf("Apple", "Blueberry", "Banana")

  val baking = Gen.oneOf("muffin", "crumble", "tart")

  val fruityBaking =
    for ( f <- fruits; d <- baking ) yield f + " " + d

  "Any fruit (except Rhubarb)" should {

    "be yummy no matter how baked!!!" in {

      forAll(fruityBaking){ fb: String =>
        println (fb)
        fb should not startWith("Rhubarb")
      }
    }
  }
}