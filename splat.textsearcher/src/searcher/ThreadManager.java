package searcher;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ThreadManager {

  private static Integer parseCount = 4;

  static ExecutorService collectService;
  static ExecutorService parseService;

  static {
    collectService = null;
    parseService = null;
  }

  public static ExecutorService getParseService(){
    if(parseService==null) parseService = Executors.newFixedThreadPool(parseCount);
    return parseService;
  }

  public static ExecutorService getCollectService(){
    if(collectService==null) collectService = Executors.newCachedThreadPool();
    return collectService;
  }

  public static void setThreadCount(Integer count){
    parseCount = count;
  }
}
