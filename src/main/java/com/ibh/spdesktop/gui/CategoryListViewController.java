/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ibh.spdesktop.gui;

import com.google.common.collect.ImmutableList;
import com.ibh.spdesktop.bl.BusinessLogic;
import com.ibh.spdesktop.dal.Category;
import com.ibh.spdesktop.message.AuthCrudMessage;
import com.ibh.spdesktop.message.MessageService;
import com.ibh.spdesktop.viewmodel.CategoryVM;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.shape.Rectangle;

/**
 * FXML Controller class
 *
 * @author ihorvath
 */
public class CategoryListViewController extends BaseController implements Initializable {

  @FXML
  private TableView<CategoryVM> categoryTable;
  @FXML
  private TableColumn<CategoryVM, String> nameColumn;
  @FXML
  private TableColumn<CategoryVM, String> colorColumn;

  @FXML
  private Label nameLabel;
  @FXML
  private Rectangle colorRectangle;

  private ObservableList<CategoryVM> data;
  private FilteredList<CategoryVM> filteredData;
  private CategoryVM currentData = null;

  public CategoryListViewController(BusinessLogic bl) {
    super(bl);
  }

  /**
   * Initializes the controller class.
   */
  @Override
  public void initialize(URL url, ResourceBundle rb) {

    List<CategoryVM> cvmlist = new ArrayList();
    getBl().getCategRepos().getList().forEach(c -> cvmlist.add(from(c)));

    data = FXCollections.observableList(cvmlist);

    nameColumn.setCellValueFactory(cellData -> cellData.getValue().getName());
    colorColumn.setCellValueFactory(cellData -> cellData.getValue().getColor());

    categoryTable.getSelectionModel().selectedItemProperty().addListener(((observable, oldvalue, newvalue) -> {
      currentData = newvalue;
      showDetails();
    }));

    filteredData = new FilteredList<>(data, p -> true);

    SortedList<CategoryVM> sortedData = new SortedList<>(filteredData);
    sortedData.comparatorProperty().bind(categoryTable.comparatorProperty());

    categoryTable.setItems(sortedData);
    if (!sortedData.isEmpty()) {
      currentData = sortedData.get(0);
    }

    showDetails();

  }

  private CategoryVM from(Category c) {
    return new CategoryVM(c.getId(), c.getName(), c.getColor());
  }

  private void showDetails() {
    if (currentData != null) {
      nameLabel.setText(currentData.getName().getValue());
      colorRectangle.setFill(currentData.getRGBColor());
    } else {
      nameLabel.setText("");
    }
  }

  @FXML
  public void handleNew() {
    MessageService.send(AuthCrudMessage.class, new AuthCrudMessage(ViewEnum.CategoryCRUDView, 0, CRUDEnum.New));
  }

  @FXML
  public void handleEdit() {
    MessageService.send(AuthCrudMessage.class, new AuthCrudMessage(ViewEnum.CategoryCRUDView, currentData.getId().get(), CRUDEnum.Update));
  }

  @FXML
  public void handleDelete() {
    MessageService.send(AuthCrudMessage.class, new AuthCrudMessage(ViewEnum.CategoryCRUDView, currentData.getId().get(), CRUDEnum.Delete));
  }

  @Override
  public void setUpValidators() {

  }
}
