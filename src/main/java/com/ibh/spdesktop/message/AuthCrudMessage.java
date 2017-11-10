/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ibh.spdesktop.message;

import com.ibh.spdesktop.gui.CRUDEnum;
import com.ibh.spdesktop.gui.ViewEnum;

/**
 *
 * @author ihorvath
 */
public class AuthCrudMessage extends BaseMessage {
  private final int authID;
  private final CRUDEnum crud;
  
  public AuthCrudMessage(ViewEnum content, int id, CRUDEnum crud) {
    super(content);
    this.authID = id;
    this.crud = crud;
  }

  public int getAuthID() {
    return authID;
  }

  public CRUDEnum getCrud() {
    return crud;
  }
 
}
