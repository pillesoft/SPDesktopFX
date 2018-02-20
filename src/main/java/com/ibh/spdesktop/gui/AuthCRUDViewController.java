package com.ibh.spdesktop.gui;

import com.ibh.spdesktop.bl.BusinessLogic;
import com.ibh.spdesktop.dal.Authentication;
import com.ibh.spdesktop.message.ActionMessage;
import com.ibh.spdesktop.message.CrudMessage;
import com.ibh.spdesktop.message.BaseMessage;
import com.ibh.spdesktop.message.MessageService;
import com.ibh.spdesktop.validation.ValidationException;
import com.ibh.spdesktop.viewmodel.AuthenticationVM;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * FXML Controller class
 *
 * @author ihorvath
 */
public class AuthCRUDViewController extends BaseController<AuthenticationVM> implements Initializable {

  private static final Logger LOG = LoggerFactory.getLogger(AuthCRUDViewController.class);
  private Authentication instance;
  private AuthenticationVM vm;

  @FXML
  private TextField txtTitle;
  @FXML
  private ComboBox<String> cmbCategory;
  @FXML
  private TextField txtUserName;
  @FXML
  private TextField txtPassword;
  @FXML
  private TextField txtWebAddress;
  @FXML
  private DatePicker dpValidFrom;
  @FXML
  private TextArea txaDescription;

  @FXML
  public void handleSave() {

    setControlStateNormal();

    try {
      vm.validateModel();
      
      
      
      MessageService.send(ActionMessage.class, new ActionMessage(ViewEnum.AuthListView));
    } catch (ValidationException exc) {
      setControlStateError(exc);
    }

  }

  @FXML
  public void handleCancel() {
    MessageService.send(ActionMessage.class, new ActionMessage(ViewEnum.AuthListView));
  }

  public AuthCRUDViewController(BusinessLogic bl) {
    super(bl);
  }

  /**
   * Initializes the controller class.
   */
  @Override
  public void initialize(URL url, ResourceBundle rb) {
    // TODO
    vm = new AuthenticationVM();
    List<String> categlist = getBl().getCategRepos()
            .getList().stream().map(c -> c.getName()).collect(Collectors.toList());

    cmbCategory.setItems(FXCollections.observableList(categlist));

    setUpValidators();
  }

  @Override
  protected void setMessage(BaseMessage message) {
    super.setMessage(message);

    // the message type is AuthCrudMessage
    CrudMessage msg = (CrudMessage) message;
    switch (msg.getCrud()) {
      case New:
        setInstance(new Authentication());
        break;
      case Update:
        setInstance(getBl().getAuthRepos().getById(msg.getId()));
        break;
      case Delete:
        setInstance(getBl().getAuthRepos().getById(msg.getId()));
        break;
      default:
        break;
    }

  }

  public Authentication getInstance() {
    return instance;
  }

  public void setInstance(Authentication instance) {
    this.instance = instance;
  }

  @Override
  public void setUpValidators() {
    try {
      setUpValidator(txtTitle, vm.getTitle());
      setUpValidator(cmbCategory, vm.getCategory());
      setUpValidator(txtUserName, vm.getUserName());
      setUpValidator(txtPassword, vm.getPassword());
      setUpValidator(txtWebAddress, vm.getWebUrl());
      setUpValidator(dpValidFrom, vm.getValidFrom());
      setUpValidator(txaDescription, vm.getDescription());

      setControlStateNormal();
    } catch (Exception ex) {
      LOG.error(ex.getMessage(), ex);
    }
  }

}
