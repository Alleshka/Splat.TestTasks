package searcher;

import java.io.File;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.stream.Collectors;

public class TextSearcher implements Callable<List<String>> {

  private String extension;
  private String directory;
  private String message;
  private Integer fileBlocksSize = 10;

  public TextSearcher(String dir, String ext, String msg){
    this.extension = ext;
    this.directory = dir;
    this.message = msg;
  }

  @Override
  public List<String> call() throws Exception {
    return start(new File(this.directory), this.extension, this.message);
  }

  private List<String> start(File dir, String ext, String msg) throws Exception {
    List<File> fileList = getFileList(dir, ext);
    List<String> results = getNameOfFileWithMessage(fileList, msg);
    return results;
  }

  private List<File> getFileList(File directory, String extenison)
      throws ExecutionException, InterruptedException {
    ExecutorService service = Executors.newSingleThreadExecutor();
    Future<List<File>> futureFiles = service.submit(new FileCollector(directory, extenison));
    return futureFiles.get();
  }

  private List<String> getNameOfFileWithMessage(List<File> fileList,  String message)
      throws InterruptedException, ExecutionException {

    ExecutorService service = ThreadManager.getParseService();
    List<Callable<List<String>>> parseTasks = getTasks(fileList, message);
    List<Future<List<String>>> futuresStrings = service.invokeAll(parseTasks);

    List<String> resultStrings = new ArrayList<>();
    for(Future<List<String>> future : futuresStrings){
      resultStrings.addAll(future.get());
    }
    return  resultStrings;
  }

  private List<Callable<List<String>>> getTasks(List<File> fileList, String msg){

    int packCount = (fileList.size()/fileBlocksSize) + 1;

    List<Callable<List<String>>> parseTasks = new ArrayList<>(packCount);

    for (int i = 0; i < packCount; i++) {
      parseTasks.add(
          new FileParser(fileList.stream().skip(i * fileBlocksSize).limit(fileBlocksSize).collect(
              Collectors.toList()), this.message));

    }
    return parseTasks;
  }
}
