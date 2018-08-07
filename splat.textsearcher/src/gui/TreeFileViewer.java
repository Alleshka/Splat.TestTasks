package gui;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import javafx.scene.control.MultipleSelectionModel;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;

public class TreeFileViewer extends TreeView<String> {
  private TreeItem<String> rootItem;
  public TreeFileViewer() {
    super();
  }
  public boolean createTree(List<String> files, String root) {
    rootItem = new TreeItem<>("root");

    for (String file : files) {
      String[] parts = file.split("\\\\");
      addToItem(rootItem, Arrays.stream(parts).collect(Collectors.toList()));
    }

    super.setRoot(rootItem);
    return true;
  }
  private boolean addToItem(TreeItem<String> item, List<String> parts) {
    if (parts.size() > 1) {

      if (!item.getChildren().stream().anyMatch(x -> x.getValue().endsWith(parts.get(0)))) {
        item.getChildren().add(new TreeItem<>(parts.get(0)));
      }

      addToItem(
          item.getChildren().stream().filter(x -> x.getValue().endsWith(parts.get(0))).findFirst().get(),
          parts.stream().skip(1).collect(
              Collectors.toList()));
    } else {
      item.getChildren().add(new TreeItem<>(parts.get(0)));
      return true;
    }

    return true;
  }

  public String getSelectedPath(){
    MultipleSelectionModel<TreeItem<String>> selectionModel = this.getSelectionModel();
    TreeItem<String> curItem = selectionModel.getSelectedItem();
    StringBuilder selected = new StringBuilder(curItem.getValue());

    while (curItem.getParent()!=this.rootItem){
      curItem = curItem.getParent();
      selected.insert(0, curItem.getValue() + "\\");
    }

    return selected.toString();
  }
}
