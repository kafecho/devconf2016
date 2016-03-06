package org.kafecho.devconf

import org.scalatest.WordSpec
import org.scalatest.Matchers
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner

@RunWith(classOf[JUnitRunner])
class NaiveBlogSpec extends WordSpec with Matchers{

  "The blog" should {

    "correctly record text" in {

      val text =
        """
          |L’iPhone constitue la vache à lait du groupe.
          |Il représente plus de 60 % du chiffre d’affaires et encore davantage en termes de profits."""

      Blog.write("infrench.txt", text)
      Blog.read("infrench.txt") should be (text)
    }
  }
}