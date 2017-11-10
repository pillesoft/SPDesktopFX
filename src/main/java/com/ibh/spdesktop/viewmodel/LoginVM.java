/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ibh.spdesktop.viewmodel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import org.hibernate.validator.internal.engine.path.PathImpl;





/**
 *
 * @author ihorvath
 */
public class LoginVM {
  
  private HashMap<String, List<String>> validationErrors;

  @NotEmpty(message = "User Name is obligatory")
  @Size(min=5, message = "Too short. Minimum length is 5 characters")
  private StringProperty userName;
  @NotEmpty(message = "Passwordis obligatory")
  @Size(min=6, message = "Too short. Minimum length is 6 characters")
  private StringProperty password;

  public LoginVM() {
    userName = new SimpleStringProperty(null, "userName", "");
    password = new SimpleStringProperty(null, "password", "");
  }

  public StringProperty getUserName() {
    return userName;
  }

  public void setUserName(StringProperty userName) {
    this.userName = userName;
  }

  public StringProperty getPassword() {
    return password;
  }

  public void setPassword(StringProperty password) {
    this.password = password;
  }

  public HashMap<String, List<String>> getValidationErrors() {
    return validationErrors;
  }

  public boolean isValid() {
    validate();
    return validationErrors.isEmpty();
  }
  
  private void validate() {
    validationErrors = new HashMap<>();
    ValidatorFactory valfact = Validation.buildDefaultValidatorFactory();
    Validator validator = valfact.getValidator();
    Set<ConstraintViolation<LoginVM>> errors = validator.validate(this);

    convertErrors(errors);
  }

  private void convertErrors(Set<ConstraintViolation<LoginVM>> errors) {
    errors.stream().forEach((err) -> {
      String propname = ((PathImpl) err.getPropertyPath()).getLeafNode().getName();
      if (validationErrors.containsKey(propname)) {
        validationErrors.get(propname).add(err.getMessage());
      } else {
        List<String> v = new ArrayList<>();
        v.add(err.getMessage());
        validationErrors.put(propname, v);
      }
    });
  }
  

  
  
}
