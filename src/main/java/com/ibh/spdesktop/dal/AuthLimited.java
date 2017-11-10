/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ibh.spdesktop.dal;

import java.time.Duration;
import java.util.Calendar;
import java.util.Date;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
/**
 *
 * @author ihorvath
 */
public class AuthLimited {
  
  private final int id;
  private final StringProperty category;
  private final StringProperty title;
  private final StringProperty webaddress;
  private final ObjectProperty<Date> validfrom;
  private final StringProperty description;
  private final StringProperty color;

  public int getId() {
    return id;
  }

  public StringProperty getCategory() {
    return category;
  }

  public StringProperty getTitle() {
    return title;
  }

  public StringProperty getWebAddress() {
    return webaddress;
  }

  public ObjectProperty<Date> getValidFrom() {
    return validfrom;
  }

  public StringProperty getDescription() {
    return description;
  }

  public StringProperty getCategColor() {
    return color;
  }
  
  public IntegerProperty getNumberOfDays() {
    if (validfrom.getValue() == null) {
      return new SimpleIntegerProperty(0);
    } else {
      long timediff = (Calendar.getInstance().getTime().getTime() - validfrom.getValue().getTime());
//      Calendar diff = Calendar.getInstance();
//      diff.setTimeInMillis(timediff);
      
      java.time.Duration d = Duration.ofMillis(timediff);
      long days = d.toDays();
      
//      diff.get(Calendar.)
      return new SimpleIntegerProperty((int)days);
    }
  }
  
  public AuthLimited(int id, String category, String title, String webaddress, Date validfrom, String description, String categcolor) {
    this.id = id;
    this.category = new SimpleStringProperty(category);
    this.title = new SimpleStringProperty(title);
    this.webaddress = new SimpleStringProperty(webaddress);
    this.validfrom = new SimpleObjectProperty<>(validfrom);
    this.description = new SimpleStringProperty(description);
    this.color = new SimpleStringProperty(categcolor);
  }

 
}
