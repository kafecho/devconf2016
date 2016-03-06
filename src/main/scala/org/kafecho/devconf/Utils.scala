package org.kafecho.devconf

import org.apache.commons.io.FileUtils
import java.io.File

/**
  * Created by guillaume on 04/03/2016.
  */

object Utils {
  def writeStringToFile(f: File, s: String) = FileUtils.writeStringToFile(f,s, Platform.encoding)
  def readFileToString(f: File) = FileUtils.readFileToString(f,Platform.encoding)
}

