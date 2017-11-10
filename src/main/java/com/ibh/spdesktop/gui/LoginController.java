/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ibh.spdesktop.gui;

import com.ibh.spdesktop.bl.BusinessLogic;
import com.ibh.spdesktop.dal.IBHDatabaseException;
import com.ibh.spdesktop.message.ActionMessage;
import com.ibh.spdesktop.message.MessageService;
import com.ibh.spdesktop.viewmodel.LoginVM;
import java.net.URL;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;
import javafx.beans.property.Property;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Control;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.CornerRadii;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * FXML Controller class
 *
 * @author ihorvath
 */
public class LoginController extends BaseController implements Initializable {

  private static final Logger LOG = LoggerFactory.getLogger(LoginController.class);
  private LoginVM vm;
  private Map<String, Control> validatedControls;

  @FXML
  private TextField txtUserName;
  @FXML
  private PasswordField txtPassword;
  @FXML
  private Label lblErrorText;

  public LoginController(BusinessLogic bl) {
    super(bl);

  }

  @FXML
  private void handleLogin() {

    setControlStateNormal();
    if (!vm.isValid()) {
      setControlStateError();
    }

    lblErrorText.setText("");
//    if (!validationSupport.isInvalid()) {
    try {
      getBl().login(txtUserName.getText(), txtPassword.getText().toCharArray());
      MessageService.send(ActionMessage.class, new ActionMessage(ViewEnum.AuthListView));
    } catch (IBHDatabaseException dexc) {
      if (dexc.getStatus().equals(IBHDatabaseException.NOTAVAILABLE)) {
        lblErrorText.setText("Database is not available");
      }
    } catch (Exception exc) {
      lblErrorText.setText("Wrong User name / Password");
    }
//
//    } else {
//      validationSupport.redecorate();
//    }
  }

  @FXML
  private void handleCreateDB() {
    lblErrorText.setText("");

    LOG.debug("handleCreateDB");

//    if (!validationSupport.isInvalid()) {
    try {
      getBl().createDB(txtUserName.getText(), txtPassword.getText().toCharArray());
      getBl().login(txtUserName.getText(), txtPassword.getText().toCharArray());
      MessageService.send(ActionMessage.class, new ActionMessage(ViewEnum.AuthListView));
    } catch (IBHDatabaseException dexc) {
      if (dexc.getStatus().equals(IBHDatabaseException.NOTAVAILABLE)) {
        lblErrorText.setText("Database is not available");
      } else if (dexc.getStatus().equals(IBHDatabaseException.AVAILABLE)) {
        lblErrorText.setText("That database is available already");
      }
    } catch (Exception exc) {
      lblErrorText.setText("Wrong User name / Password");
    }
//
//    } else {
//      validationSupport.redecorate();
//    }
  }

  /**
   * Initializes the controller class.
   */
  @Override
  public void initialize(URL url, ResourceBundle rb) {

    try {
      vm = new LoginVM();

      validatedControls = new HashMap<>();

      setUpValidator(txtUserName, vm.getUserName());
      setUpValidator(txtPassword, vm.getPassword());
      
      setControlStateNormal();
      
    } catch (Exception ex) {
      LOG.error(ex.getMessage(), ex);
    }

  }

  private void setUpValidator(Control ctrl, Property field) throws Exception {
    if (ctrl instanceof TextField) {
      TextField ctrltxt = (TextField) ctrl;
      ctrltxt.textProperty().bindBidirectional(field);
      validatedControls.put(field.getName(), ctrltxt);
    } else {
      throw new Exception("not defined control");
    }

  }

  private void setControlStateNormal() {
    BorderStroke bs = new BorderStroke(Paint.valueOf("GREY"), BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT);
    Border b = new Border(bs);
    Tooltip tt = new Tooltip("");

    validatedControls.forEach((k, v) -> {
      v.borderProperty().set(b);
      v.tooltipProperty().set(tt);
    });
  }

  private void setControlStateError() {
    BorderStroke bs = new BorderStroke(Paint.valueOf("RED"), BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT);
    Border b = new Border(bs);

    vm.getValidationErrors().forEach((field, errs) -> {
      Control ctrl = validatedControls.get(field);
      ctrl.borderProperty().set(b);

      Tooltip tt = new Tooltip(String.join("\n", errs));
      ctrl.tooltipProperty().set(tt);

    });

  }
}
