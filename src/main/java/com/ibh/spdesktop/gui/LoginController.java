package com.ibh.spdesktop.gui;

import com.ibh.spdesktop.bl.BusinessLogic;
import com.ibh.spdesktop.dal.IBHDatabaseException;
import com.ibh.spdesktop.message.ActionMessage;
import com.ibh.spdesktop.message.MessageService;
import com.ibh.spdesktop.validation.ValidationException;
import com.ibh.spdesktop.viewmodel.LoginVM;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * FXML Controller class
 *
 * @author ihorvath
 */
public class LoginController extends BaseController<LoginVM> implements Initializable {

  private static final Logger LOG = LoggerFactory.getLogger(LoginController.class);
  private LoginVM vm;

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
    lblErrorText.setText("");

    try {
      vm.validateModel();
      getBl().login(txtUserName.getText(), txtPassword.getText().toCharArray());
      MessageService.send(ActionMessage.class, new ActionMessage(ViewEnum.AuthListView));
    } catch (IBHDatabaseException dexc) {
      lblErrorText.setText(dexc.getType().getDescription());
    } catch (ValidationException exc) {
      setControlStateError(exc);
    } catch (Exception exc) {
      lblErrorText.setText("Wrong User name / Password");
    }
  }

  @FXML
  private void handleCreateDB() {
    setControlStateNormal();
    lblErrorText.setText("");

    LOG.debug("handleCreateDB");

    try {
      vm.validateModel();

      getBl().createDB(txtUserName.getText(), txtPassword.getText().toCharArray());
      getBl().login(txtUserName.getText(), txtPassword.getText().toCharArray());
      MessageService.send(ActionMessage.class, new ActionMessage(ViewEnum.AuthListView));
    } catch (ValidationException exc) {
      setControlStateError(exc);
    } catch (IBHDatabaseException dexc) {
      lblErrorText.setText(dexc.getType().getDescription());
    } catch (Exception exc) {
      lblErrorText.setText("Wrong User name / Password");
    }
  }

  /**
   * Initializes the controller class.
   */
  @Override
  public void initialize(URL url, ResourceBundle rb) {
    vm = new LoginVM();
//    setInstance(vm);

    setUpValidators();
  }

  @Override
  public void setUpValidators() {
    try {
      setUpValidator(txtUserName, vm.getUserName());
      setUpValidator(txtPassword, vm.getPassword());

      setControlStateNormal();
    } catch (Exception ex) {
      LOG.error(ex.getMessage(), ex);
    }

  }

//  @Override
//  public void setInstance(BaseViewModel vm) {
//    
//  }
}
