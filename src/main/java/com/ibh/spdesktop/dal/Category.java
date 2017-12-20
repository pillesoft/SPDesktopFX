/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ibh.spdesktop.dal;

import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;


/**
 *
 * @author ihorvath
 */
@Entity
@Table(name = "CATEGORIES_DICT")
@NamedQuery(name = "getCategoryAll", query = "SELECT OBJECT(c) from Category c")
public class Category implements Serializable {

  private static final long serialVersionUID = 1L;
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "ID", nullable = false)
  private Integer id;
  
  @Column(name = "NAME", unique = true, length = 100)
  private String name;
  @Column(name = "COLOR", length = 15)
  private String color;
  
  public Category() {
  }

  public Category(String name) {
    this.name = name;
    this.color = "0xffffffff";
  }

  public Category(Integer id, String name, String hexcolor) {
    this.id = id;
    this.name = name;
    this.color = hexcolor;
  }

  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getColor() {
    return color;
  }

  public void setColor(String color) {
    this.color = color;
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
    if (!(o instanceof Category)) {
      return false;
    }
    Category categ = (Category) o;
    return Objects.equals(id, categ.id);
  }
}
