package searcher;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

public class FileParser implements Callable<List<String>> {

  private List<File> curFiles;
  private String message;

  public FileParser(List<File> files, String msg) {
    curFiles = files;
    this.message = msg;
  }

  @Override
  public List<String> call() throws Exception {
    return parse(this.curFiles);
  }

  private List<String> parse(List<File> files){
    List<String> resultFiles = new ArrayList<>();
    for(File file : files){
      if(isContainMessage(file, this.message)) resultFiles.add(file.getAbsolutePath());
    }
    return resultFiles;
  }
  private boolean isContainMessage(File file, String msg){
    String line;
    try(BufferedReader reader = new BufferedReader(new FileReader(file))){
      while ((line = reader.readLine())!=null){
        if(line.contains(msg)) return true;
      }
    } catch (FileNotFoundException e) {
      //e.printStackTrace();
      return false;
    } catch (IOException e) {
       //e.printStackTrace();
      return false;
    }
    return false;
  }
}
