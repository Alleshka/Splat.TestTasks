package searcher;

import fiefilters.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

public class FileCollector implements Callable<List<File>> {

  private File directory;
  private String extension;

  private FileFilter dirFilter;
  private FileFilter extFilter;

  public FileCollector(File dir, String ext) {
    this.directory = dir;
    this.extension = ext;

    this.dirFilter = new DirectoryFilter();
    this.extFilter = new ExtensionFileFilter(ext);
  }

  @Override
  public List<File> call() throws Exception {
    return getFileList();
  }

  private List<File> getFileList() {
    ArrayList<File> fileList = new ArrayList<>();

    // TODO: Вынести объявление
    ExecutorService service = ThreadManager.getCollectService();
    for (File file : getFiles(this.directory)) {
      try {
        fileList.add(file);
      }
      catch (Exception ex) {

      }
    }

    File[] directories = getDirectories(this.directory);
    Future<List<File>>[] futures = new Future[directories.length];

    for (int i = 0; i < directories.length; i++) {
      futures[i] = service.submit(new FileCollector(directories[i], this.extension));
    }

    for (int i = 0; i < futures.length; i++) {
      try {
        fileList.addAll(futures[i].get());
      } catch (InterruptedException e) {
        //e.printStackTrace();
      } catch (ExecutionException e) {
        //e.printStackTrace();
      }
    }
    return fileList;
  }

  private File[] getDirectories(File dir) {
    return dir.listFiles(dirFilter);
  }

  private File[] getFiles(File dir) {
    return dir.listFiles(extFilter);
  }
}
