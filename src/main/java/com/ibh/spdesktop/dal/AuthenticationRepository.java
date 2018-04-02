/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ibh.spdesktop.dal;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import com.ibh.spdesktop.viewmodel.AuthLimitedVM;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 *
 * @author ihorvath
 */
public class AuthenticationRepository extends BaseRepository<Authentication> {

  public ObservableList<AuthLimitedVM> getAuthLimited() {
    
    EntityManager em = DbContext.getEM();
    List<?> ret;
    
    try {
      em.getTransaction().begin();
      Query q = em.createNamedQuery("getAuthLimited", AuthLimitedVM.class);
      ret = q.getResultList();
      em.getTransaction().commit();
    } catch (Exception exc) {
      throw exc;
    } finally {
      if (em.isOpen()) {
        em.close();
      }
    }

    ArrayList<AuthLimitedVM> listal = new ArrayList<>();
    
    for (Object r : ret) {
      Object[] row = (Object[])r;
      
      String webaddr = "";
      if (row[2] != null) {
        webaddr = row[2].toString();
      }
      String descr = "";
      if (row[4] != null) {
        descr = row[4].toString();
      }
      
      LocalDate d = null;
      if (row[3] != null) {
        d = (LocalDate)row[3];
      }
      
      AuthLimitedVM al = new AuthLimitedVM(Integer.decode(row[5].toString()), row[0].toString(), row[1].toString(), webaddr, descr, d, row[6].toString());
      listal.add(al);
    }
    
    return FXCollections.observableList(listal);
  }
  
  public AuthInfo getAuthInfo(int id) {
    Authentication a = getById(id);
    AuthInfo ret = new AuthInfo(a.getId(), a.getUsername(), a.getPassword(), a.getTitle());
    return ret;
  }

  @Override
  public String getTableName() {
    throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
  }
  
  
          
}
