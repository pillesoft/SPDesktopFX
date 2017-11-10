/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ibh.spdesktop.dal;

import java.util.Date;
import java.util.ArrayList;
import java.util.List;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javax.persistence.EntityManager;
import javax.persistence.Query;

/**
 *
 * @author ihorvath
 */
public class AuthenticationRepository extends BaseRepository<Authentication> {

  public ObservableList<AuthLimited> getAuthLimited() {
    
    EntityManager em = DbContext.getEM();
    List<AuthLimited> ret;
    
    try {
      em.getTransaction().begin();
      Query q = em.createNamedQuery("getAuthLimited", AuthLimited.class);
      ret = q.getResultList();
      em.getTransaction().commit();
    } catch (Exception exc) {
      throw exc;
    } finally {
      if (em.isOpen()) {
        em.close();
      }
    }

    ArrayList<AuthLimited> listal = new ArrayList<>();
    
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
      
      Date d = null;
      if (row[3] != null) {
        d = (Date)row[3];
      }
      
      AuthLimited al = new AuthLimited(Integer.decode(row[5].toString()), row[1].toString(), row[0].toString(), webaddr, d, descr, row[6].toString());
      listal.add(al);
    }
    
    return FXCollections.observableList(listal);
  }
  
  @Override
  public Authentication getById(int id) {
    Authentication a = super.getById(id);
//    a.setPwdClearInit();
    
    return a;
  }
  
  public AuthInfo getAuthInfo(int id) {
    Authentication a = getById(id);
    AuthInfo ret = new AuthInfo(a.getId(), a.getUsername(), a.getPassword(), a.getTitle());
    return ret;
  }

  @Override
  public void update(Authentication obj) throws IBHDbConstraintException {
    Authentication old = getById(obj.getId());

//    Session sess = DbContext.getSessionFactory().openSession();
//    sess.beginTransaction();
//    try {
//      if (obj.getIsPwdChanged()) {
//        // make pwdchnaged
//        AuthPwdHistory hist = new AuthPwdHistory(obj, old.getPassword());
//        sess.save(hist);
//      }
//      sess.merge(obj);
//      sess.getTransaction().commit();
//    } catch (ConstraintViolationException exc) {
//      throw parseConstraintExc(exc);
//    } finally {
//      sess.close();
//    }
  }

  @Override
  public String getTableName() {
    throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
  }
  
  
          
}
