package com.ibh.spdesktop.gui;

import com.ibh.spdesktop.bl.BusinessLogic;
import com.ibh.spdesktop.message.ActionMessage;
import com.ibh.spdesktop.message.CrudMessage;
import com.ibh.spdesktop.message.BaseMessage;
import com.ibh.spdesktop.message.MessageService;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.ToolBar;
import javafx.scene.layout.BorderPane;

/**
 *
 * @author ihorvath
 */
public class MainController extends BaseController implements Initializable {

  @FXML
  private BorderPane borderPane;
  @FXML
  private ToolBar toolbar;

  private BooleanProperty isToolbarDisabled;

  public MainController(BusinessLogic bl) {
    super(bl);
  }

  @Override
  public void initialize(URL url, ResourceBundle rb) {

    setBundle(rb);

    isToolbarDisabled = new SimpleBooleanProperty(true);
    toolbar.disableProperty().bind(isToolbarDisabled);

    MessageService.register(ActionMessage.class, (arg) -> {
      ActionMessage actmess = (ActionMessage) arg;
      switch (actmess.getContent()) {
        case AuthListView:
          postLogin();
          break;
        default:
          break;
      }

      setContentCenter(actmess.getContent(), actmess);

    });

    MessageService.register(CrudMessage.class, (arg) -> {
      CrudMessage actmess = (CrudMessage) arg;
      setContentCenter(actmess.getContent(), actmess);

    });

    setContentCenter(ViewEnum.Login, null);

  }

  private void setContentCenter(ViewEnum uitype, BaseMessage msg) {
    //borderPane.centerProperty().setValue(null);

    Node node;
    try {
      View v = ViewFactory.getViews().get(uitype);

      FXMLLoader loader = new FXMLLoader(getClass().getResource(v.getFxmlPath()), getBundle());
      BaseController controller = (BaseController) v.getController().getDeclaredConstructor(BusinessLogic.class).newInstance(getBl());
      controller.setMessage(msg);
      loader.setController(controller);
      node = loader.load();

      borderPane.centerProperty().setValue(node);

    } catch (IOException ex) {
      Logger.getLogger(MainController.class.getName()).log(Level.SEVERE, null, ex);
    } catch (NoSuchMethodException ex) {
      Logger.getLogger(MainController.class.getName()).log(Level.SEVERE, null, ex);
    } catch (SecurityException ex) {
      Logger.getLogger(MainController.class.getName()).log(Level.SEVERE, null, ex);
    } catch (InstantiationException ex) {
      Logger.getLogger(MainController.class.getName()).log(Level.SEVERE, null, ex);
    } catch (IllegalAccessException ex) {
      Logger.getLogger(MainController.class.getName()).log(Level.SEVERE, null, ex);
    } catch (IllegalArgumentException ex) {
      Logger.getLogger(MainController.class.getName()).log(Level.SEVERE, null, ex);
    } catch (InvocationTargetException ex) {
      Logger.getLogger(MainController.class.getName()).log(Level.SEVERE, null, ex);
    }
  }

  private void postLogin() {
    System.out.println("postLogin");
    isToolbarDisabled.setValue(false);
  }

  @FXML
  public void handleTlbExit() {
    Platform.exit();
  }

  @FXML
  public void handleTlbAuth() {
    setContentCenter(ViewEnum.AuthListView, null);
  }

  @FXML
  public void handleTlbCateg() {
    setContentCenter(ViewEnum.CategoryListView, null);
  }

  @FXML
  public void handleTlbAbout() {

  }

  @Override
  public void setUpValidators() {

  }

}
