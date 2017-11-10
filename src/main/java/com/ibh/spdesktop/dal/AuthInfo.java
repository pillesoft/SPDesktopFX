/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ibh.spdesktop.dal;

import com.ibh.spdesktop.bl.Crypt;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author ihorvath
 */
public class AuthInfo {
  private final int id;
  private final String username;
  private final String title;
  private String pwdclear;

  public int getId() {
    return id;
  }

  public String getUsername() {
    return username;
  }

  public String getTitle() {
    return title;
  }

  public String getPwdClear() {
    return pwdclear;
  }
  
  public AuthInfo(int id, String userName, String password, String title) {
    this.id = id;
    this.username = userName;
    this.title = title;
    this.pwdclear = "";
    
    try {
      if (password != null) {
        this.pwdclear = new String(Crypt.decrypt(password));
      }
    } catch (InvalidKeyException ex) {
      Logger.getLogger(AuthInfo.class.getName()).log(Level.SEVERE, null, ex);
    } catch (InvalidAlgorithmParameterException ex) {
      Logger.getLogger(AuthInfo.class.getName()).log(Level.SEVERE, null, ex);
    }

  }
  
}
