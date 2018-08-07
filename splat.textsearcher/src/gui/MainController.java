package gui;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import searcher.TextSearcher;
import searcher.ThreadManager;

public class MainController {

  @FXML
  private TextField pathDir;
  @FXML
  private TextField extension;
  @FXML
  private TextField message;
  @FXML
  private TreeFileViewer tree;
  @FXML
  private TabPane fileTab;

  @FXML
  private void search(ActionEvent event) throws ExecutionException, InterruptedException {
    ExecutorService gui = ThreadManager.getGuiService();
    Future<List<String>> future = gui
        .submit(new TextSearcher(pathDir.getText(), extension.getText(), message.getText()));
        tree.createTree(future.get(), pathDir.getText());
  }

  @FXML
  private void selectChange(ActionEvent event){
    TextArea fileContent = new TextArea();
    String filePath = tree.getSelectedPath();

    try(BufferedReader reader = new BufferedReader(new FileReader(filePath))){
      String line;
      while ((line=reader.readLine())!=null){
        fileContent.appendText(line + "\n");
      }
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    }

    Tab tabFile = new Tab();
    tabFile.setText(filePath);
    tabFile.setContent(fileContent);
    this.fileTab.getTabs().add(tabFile);
  }
}
