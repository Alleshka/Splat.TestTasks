package gui;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;

public class MainController {
  @FXML
  private Button btnSearch;

  @FXML
  private void click(ActionEvent event){
    Alert messageBox = new Alert(AlertType.INFORMATION);
    messageBox.setTitle("TITLE");
    messageBox.setContentText("Hello, world");
    messageBox.show();
  }
}
