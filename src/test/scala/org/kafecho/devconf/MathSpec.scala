package org.kafecho.devconf

import org.scalatest.Matchers
import org.scalatest.WordSpec
import org.scalatest.junit.JUnitRunner
import org.scalatest.prop.GeneratorDrivenPropertyChecks
import org.junit.runner.RunWith
import org.scalacheck.Arbitrary

@RunWith(classOf[JUnitRunner])
class MathSpec extends WordSpec with Matchers with GeneratorDrivenPropertyChecks {

  val numbers = (Arbitrary.arbitrary[Int]).filter( n => n >= 0)

  "The square root of a number squared" should {

    "always be the number itself" in {

      forAll(numbers){ n =>
        Math.sqrt( n * n ) should be (n)
      }
    }
  }
}