/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ibh.spdesktop.viewmodel;

import com.ibh.spdesktop.validation.ValidationException;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.paint.Color;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author ihorvath
 */
public class CategoryVM extends BaseViewModel<CategoryVM> {

  private IntegerProperty id;
  @NotNull
  @Size(max = 100)
  private StringProperty name;
  private StringProperty color;

  public CategoryVM() {
    this.id = new SimpleIntegerProperty(null, "id", 0);
    this.name = new SimpleStringProperty(null, "name", "");
    this.color = new SimpleStringProperty(null, "color", "");
  }

  public CategoryVM(int id, String name, String color) {
    this.id = new SimpleIntegerProperty(null, "id", id);
    this.name = new SimpleStringProperty(null, "name", name);
    this.color = new SimpleStringProperty(null, "color", color);
  }

  public IntegerProperty getId() {
    return id;
  }

  public void setId(IntegerProperty id) {
    this.id = id;
  }

  public StringProperty getName() {
    return name;
  }

  public void setName(StringProperty name) {
    this.name = name;
  }

  public StringProperty getColor() {
    return color;
  }

  public void setColor(StringProperty color) {
    this.color = color;
  }

  public Color getRGBColor() {
    return Color.valueOf(color.get());
  }

  @Override
  public void validateModel() throws ValidationException {
    super.validate();
  }

}
