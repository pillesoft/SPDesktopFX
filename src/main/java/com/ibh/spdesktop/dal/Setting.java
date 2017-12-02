package com.ibh.spdesktop.dal;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

/**
 *
 * @author ihorvath
 */
@Entity
@Table(name = "SETTING")
@NamedQueries({
  @NamedQuery(name = "getByName", query = "SELECT s from Setting s where s.name = :name"),
})
public class Setting implements Serializable {

  private static final long serialVersionUID = 1L;
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "ID", nullable = false)
  private Integer id;
  @Column(name = "NAME", unique = true, length = 50)
  private String name;
  @Column(name = "VALUE", length = 150)
  private String value;

  public Setting() {
  }

  public Setting(String name, String value) {
    this.name = name;
    this.value = value;
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

  public String getValue() {
    return value;
  }

  public void setValue(String value) {
    this.value = value;
  }

  
  
}
