package org.kafecho.devconf

import org.scalatest.WordSpec
import org.scalatest.Matchers
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import org.scalatest.prop.GeneratorDrivenPropertyChecks
import java.nio.charset.Charset

@RunWith(classOf[JUnitRunner])
class ForeignCharactersBlogSpec extends WordSpec with Matchers with GeneratorDrivenPropertyChecks {

  "The blog" should {

    "correctly record text with 'foreign' characters" in {

      val text = "安倍 晋三 is having lunch with 习近平 anb ประยุทธ์ จันทร์โอชา at the Davos summit."

      Blog.write("davos.txt", text)
      Blog.read("davos.txt") should be (text)
    }
  }
}