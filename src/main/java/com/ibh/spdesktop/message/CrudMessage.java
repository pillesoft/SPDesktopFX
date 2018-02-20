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
public class CrudMessage extends BaseMessage {
  private final int id;
  private final CRUDEnum crud;
  
  public CrudMessage(ViewEnum content, int id, CRUDEnum crud) {
    super(content);
    this.id = id;
    this.crud = crud;
  }

  public int getId() {
    return id;
  }

  public CRUDEnum getCrud() {
    return crud;
  }
 
}
