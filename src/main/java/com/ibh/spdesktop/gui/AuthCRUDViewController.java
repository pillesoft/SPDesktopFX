/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ibh.spdesktop.gui;

import com.ibh.spdesktop.bl.BusinessLogic;
import com.ibh.spdesktop.dal.Authentication;
import com.ibh.spdesktop.message.ActionMessage;
import com.ibh.spdesktop.message.AuthCrudMessage;
import com.ibh.spdesktop.message.BaseMessage;
import com.ibh.spdesktop.message.MessageService;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;

/**
 * FXML Controller class
 *
 * @author ihorvath
 */
public class AuthCRUDViewController extends BaseController implements Initializable {

  private Authentication instance;
  
  @FXML
  public void handleSave() {
    MessageService.send(ActionMessage.class, new ActionMessage(ViewEnum.AuthListView));
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
  }  

  @Override
  protected void setMessage(BaseMessage message) {
    super.setMessage(message);
    
    // the message type is AuthCrudMessage
    AuthCrudMessage msg = (AuthCrudMessage)message;
    switch (msg.getCrud()) {
      case New:
        setInstance(new Authentication());
        break;
      case Update:
        setInstance(getBl().getAuthRepos().getById(msg.getAuthID()));
        break;
      case Delete:
        setInstance(getBl().getAuthRepos().getById(msg.getAuthID()));
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
  
  
}
