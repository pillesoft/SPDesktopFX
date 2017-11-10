/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ibh.spdesktop.dal;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 *
 * @author ihorvath
 */
@Entity
@Table(name = "AUTHPROPERTY")
public class AuthProperty implements Serializable {

  private static final long serialVersionUID = 1L;
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "ID", nullable = false)
  private Integer id;

  @ManyToOne()
  @JoinColumn(name = "AUTHENTICATION_ID", nullable = false)
//  @OnDelete(action = OnDeleteAction.CASCADE)
  private Authentication authentication;
    
  @Column(name = "PROPNAME", nullable = false, length = 100)
  private String propertyName;
  
  @Column(name = "PROPVALUE", nullable = false, length = 100)
  private String propertyValue;
  
  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  public String getPropertyName() {
    return propertyName;
  }

  public void setPropertyName(String propertyName) {
    this.propertyName = propertyName;
  }

  public String getPropertyValue() {
    return propertyValue;
  }

  public void setPropertyValue(String propertyValue) {
    this.propertyValue = propertyValue;
  }

  public Authentication getAuthentication() {
    return authentication;
  }

  public void setAuthentication(Authentication authentication) {
    this.authentication = authentication;
  }

  public AuthProperty(Authentication authentication, String propertyName, String propertyValue) {
    this.authentication = authentication;
    this.propertyName = propertyName;
    this.propertyValue = propertyValue;
  }

  public AuthProperty() {
  }

  
  @Override
  public int hashCode() {
    int hash = 0;
    hash += (id != null ? id.hashCode() : 0);
    return hash;
  }

  @Override
  public boolean equals(Object object) {
    // TODO: Warning - this method won't work in the case the id fields are not set
    if (!(object instanceof AuthProperty)) {
      return false;
    }
    AuthProperty other = (AuthProperty) object;
    if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
      return false;
    }
    return true;
  }

  @Override
  public String toString() {
    return "com.ibh.spdesktop.dal.AuthProperty[ id=" + id + " ]";
  }
  
}
