/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ibh.spdesktop.gui;

import com.ibh.spdesktop.bl.BusinessLogic;
import com.ibh.spdesktop.message.BaseMessage;
import java.util.ResourceBundle;


/**
 *
 * @author ihorvath
 */
public abstract class BaseController {
  private final BusinessLogic bl;
  private ResourceBundle bundle;
  private BaseMessage message;

  protected BusinessLogic getBl() {
    return bl;
  }
  
  protected ResourceBundle getBundle() {
    return bundle;
  }

  protected void setBundle(ResourceBundle bundle) {
    this.bundle = bundle;
  }

  protected void setMessage(BaseMessage message) {
    this.message = message;
  }

  public BaseController(BusinessLogic bl) {
    this.bl = bl;
  }

  
  
}
