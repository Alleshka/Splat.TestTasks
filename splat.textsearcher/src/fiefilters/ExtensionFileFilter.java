package fiefilters;

import java.io.File;
import java.io.FileFilter;

public class ExtensionFileFilter implements FileFilter {

  private String extension;

  public ExtensionFileFilter(String ext){
    this.extension = ext;
  }

  @Override
  public boolean accept(File pathname) {
    return pathname.getPath().endsWith(this.extension);
  }
}
