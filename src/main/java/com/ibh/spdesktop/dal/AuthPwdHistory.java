/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ibh.spdesktop.dal;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;

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
@Entity()
@Table(name = "AUTHPWDHISTORY")
public class AuthPwdHistory  implements Serializable {

	private static final long serialVersionUID = 1L;

@Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "ID", nullable = false)
  private Integer id;

  @ManyToOne()
  @JoinColumn(name = "AUTHENTICATION_ID", nullable = false)
//  @OnDelete(action = OnDeleteAction.CASCADE)
  private final Authentication authentication;
  
  @Column(name = "PASSWORD", length = 200)
  private final String password;

  @Column(name = "EXPIRED")
  private final LocalDateTime expired;

  public AuthPwdHistory(Authentication authentication, String password) {
    this.authentication = authentication;
    this.password = password;
    this.expired = LocalDateTime.now();
  }

  public AuthPwdHistory() {
    this.authentication = null;
    this.password = "";
    this.expired = LocalDateTime.now();
  }

  
  public Integer getId() {
    return id;
  }

  public Authentication getAuthentication() {
    return authentication;
  }

  public String getPassword() {
    return password;
  }

  public LocalDateTime getExpired() {
    return expired;
  }
  
  @Override
  public int hashCode() {
    return Objects.hash(id);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof AuthPwdHistory)) {
      return false;
    }
    AuthPwdHistory authhist = (AuthPwdHistory)o;
    return Objects.equals(id, authhist.id);
  }

  
}
