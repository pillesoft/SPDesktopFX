/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ibh.spdesktop.gui;

import com.ibh.spdesktop.bl.BusinessLogic;
import com.ibh.spdesktop.dal.AuthLimited;
import com.ibh.spdesktop.message.CrudMessage;
import com.ibh.spdesktop.message.MessageService;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

/**
 * FXML Controller class
 *
 * @author ihorvath
 */
public class AuthListViewController extends BaseController implements Initializable {

  @FXML
  private TableView<AuthLimited> authTable;
  @FXML
  private TableColumn<AuthLimited, String> categoryColumn;
  @FXML
  private TableColumn<AuthLimited, String> titleColumn;

  @FXML
  private TextField categoryFilter;
  @FXML
  private TextField titleFilter;

  @FXML
  private Label categoryLabel;
  @FXML
  private Label titleLabel;
  @FXML
  private Hyperlink webAddressLabel;
  @FXML
  private Label howOldLabel;
  @FXML
  private TextArea descriptionLabel;

  private ObservableList<AuthLimited> data;
  private FilteredList<AuthLimited> filteredData;
  private AuthLimited currentData = null;

  public AuthListViewController(BusinessLogic bl) {
    super(bl);
  }

  /**
   * Initializes the controller class.
   */
  @Override
  public void initialize(URL url, ResourceBundle rb) {

    data = getBl().getAuthRepos().getAuthLimited();

    categoryColumn.setCellValueFactory(cellData -> cellData.getValue().getCategory());
    titleColumn.setCellValueFactory(cellData -> cellData.getValue().getTitle());

    authTable.getSelectionModel().selectedItemProperty().addListener(((observable, oldvalue, newvalue) -> {
      currentData = newvalue;
      showDetails();
    }));

    filteredData = new FilteredList<>(data, p -> true);

    categoryFilter.textProperty().addListener((observable, oldValue, newValue) -> {
      doFilter();
    });

    titleFilter.textProperty().addListener((obs, oldv, newv) -> {
      doFilter();
    });

    SortedList<AuthLimited> sortedData = new SortedList<>(filteredData);
    sortedData.comparatorProperty().bind(authTable.comparatorProperty());

    authTable.setItems(sortedData);
    if (!sortedData.isEmpty()) {
      currentData = sortedData.get(0);
    }

    showDetails();

  }

  private void showDetails() {
    if (currentData != null) {
      categoryLabel.setText(currentData.getCategory().getValue());
      titleLabel.setText(currentData.getTitle().getValue());
      webAddressLabel.setText(currentData.getWebAddress().getValue());
      howOldLabel.setText(Integer.toString(currentData.getNumberOfDays().getValue()));
      descriptionLabel.setText(currentData.getDescription().getValue());
    } else {
      categoryLabel.setText("");
      titleLabel.setText("");
      webAddressLabel.setText("");
      howOldLabel.setText("");
      descriptionLabel.setText("");
    }
  }

  @FXML
  public void handleNew() {
    MessageService.send(CrudMessage.class, new CrudMessage(ViewEnum.AuthCRUDView, 0, CRUDEnum.New));
  }

  @FXML
  public void handleEdit() {
    MessageService.send(CrudMessage.class, new CrudMessage(ViewEnum.AuthCRUDView, currentData.getId(), CRUDEnum.Update));
  }

  @FXML
  public void handleDelete() {
    MessageService.send(CrudMessage.class, new CrudMessage(ViewEnum.AuthCRUDView, currentData.getId(), CRUDEnum.Delete));
  }

  @FXML
  public void handleClearFilter() {
    categoryFilter.clear();
    titleFilter.clear();
    filteredData.setPredicate(a -> true);
  }

  @FXML
  public void handleViewAuth() {

  }

  @FXML
  public void handleWebAddressLink() {
    String command = String.format("start %s %s", "firefox", webAddressLabel.getText());
    try {
      Runtime.getRuntime().exec(new String[]{"cmd", "/c", command});
      // this is linux
      //Runtime.getRuntime().exec(new String[] { "chromium-browser", "http://example.com/" });
    } catch (IOException ex) {
      Logger.getLogger(AuthListViewController.class.getName()).log(Level.SEVERE, null, ex);
    }
  }

  private void doFilter() {
    String cfilter = categoryFilter.getText();
    String tfilter = titleFilter.getText();

    filteredData.setPredicate(a -> {

      String lowercFilter = cfilter.toLowerCase();
      String lowertFilter = tfilter.toLowerCase();
      // none of empty
      if (((cfilter != null && !cfilter.isEmpty()) && a.getCategory().getValue().toLowerCase().contains(lowercFilter))
              && ((tfilter != null && !tfilter.isEmpty()) && a.getTitle().getValue().toLowerCase().contains(lowertFilter))) {
        return true;
      } else if (((cfilter != null && !cfilter.isEmpty()) && a.getCategory().getValue().toLowerCase().contains(lowercFilter))
              && (tfilter == null || tfilter.isEmpty())) {
        // title is empty
        return true;
      } else if ((cfilter == null || cfilter.isEmpty())
              && ((tfilter != null && !tfilter.isEmpty()) && a.getTitle().getValue().toLowerCase().contains(lowertFilter))) {
        // category is empty
        return true;
      }
      return false;
    }
    );

  }

  @Override
  public void setUpValidators() {
    
  }
}
