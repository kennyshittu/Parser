package com.ef.utils;

import java.io.File;
import net.sourceforge.argparse4j.inf.ArgumentChoice;
import org.apache.commons.lang3.StringUtils;

//This is a custom Argument checker to validate File path existence and readability
public class FileArgumentChecker implements ArgumentChoice {

  @Override
  public boolean contains(Object val) {
    File file = (File) val;
    String errorMsg = "";
    if (file.exists()) {
      if (!file.canRead()) {
        errorMsg = StringUtils.join("Can't read data from file : ", file.getName());
        throw new IllegalArgumentException(errorMsg);
      }
    } else {
      errorMsg = StringUtils.join("File not found, Invalid path : ", file.getAbsolutePath());
      throw new IllegalArgumentException(errorMsg);
    }
    return true;
  }

  @Override
  public String textualFormat() {
    return null;
  }
}
