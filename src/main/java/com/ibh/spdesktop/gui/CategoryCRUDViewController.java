package com.ibh.spdesktop.gui;

import com.ibh.spdesktop.bl.BusinessLogic;
import com.ibh.spdesktop.dal.Category;
import com.ibh.spdesktop.message.ActionMessage;
import com.ibh.spdesktop.message.AuthCrudMessage;
import com.ibh.spdesktop.message.BaseMessage;
import com.ibh.spdesktop.message.MessageService;
import com.ibh.spdesktop.validation.ValidationException;
import com.ibh.spdesktop.viewmodel.CategoryVM;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * FXML Controller class
 *
 * @author ihorvath
 */
public class CategoryCRUDViewController extends BaseController<CategoryVM> implements Initializable {

  private static final Logger LOG = LoggerFactory.getLogger(CategoryCRUDViewController.class);
  private Category instance;
  private CategoryVM vm;

  @FXML
  private TextField txtName;
  @FXML
  private ColorPicker cpColor;

  @FXML
  public void handleSave() {

    setControlStateNormal();

    try {
      vm.validateModel();

      AuthCrudMessage msg = (AuthCrudMessage) getMessage();
      switch (msg.getCrud()) {
        case New:
          instance = fromVMToEntity();
          getBl().getCategRepos().add(instance);
        case Update:
          break;
        case Delete:
          break;
        default:
          throw new AssertionError(msg.getCrud().name());

      }

      MessageService.send(ActionMessage.class, new ActionMessage(ViewEnum.CategoryListView));
    } catch (ValidationException exc) {
      setControlStateError(exc);
    }

  }

  @FXML
  public void handleCancel() {
    MessageService.send(ActionMessage.class, new ActionMessage(ViewEnum.CategoryListView));
  }

  public CategoryCRUDViewController(BusinessLogic bl) {
    super(bl);
  }

  /**
   * Initializes the controller class.
   */
  @Override
  public void initialize(URL url, ResourceBundle rb) {
    setUpValidators();
  }

  @Override
  protected void setMessage(BaseMessage message) {
    super.setMessage(message);

    // the message type is AuthCrudMessage
    AuthCrudMessage msg = (AuthCrudMessage) message;
    switch (msg.getCrud()) {
      case New:
//        setInstance(new Authentication());
        vm = new CategoryVM();

        break;
      case Update:
//        setInstance(getBl().getAuthRepos().getById(msg.getAuthID()));
        break;
      case Delete:
//        setInstance(getBl().getAuthRepos().getById(msg.getAuthID()));
        break;
      default:
        break;
    }

  }

  public Category getInstance() {
    return instance;
  }

  public void setInstance(Category instance) {
    this.instance = instance;
  }

  @Override
  public void setUpValidators() {
    try {
      setUpValidator(txtName, vm.getName());

      setControlStateNormal();
    } catch (Exception ex) {
      LOG.error(ex.getMessage(), ex);
    }
  }

  private CategoryVM fromEntityToVM(Category c) {
    return new CategoryVM(c.getId(), c.getName(), c.getColor());
  }

  private Category fromVMToEntity() {
    Color c = cpColor.getValue();
    return new Category(vm.getId().get(), vm.getName().get(), c.toString());
  }

}
