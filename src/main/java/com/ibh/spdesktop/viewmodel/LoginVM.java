package com.ibh.spdesktop.viewmodel;

import com.ibh.spdesktop.validation.ValidationException;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;


/**
 *
 * @author ihorvath
 */
public class LoginVM extends BaseViewModel<LoginVM> {
  

  @NotEmpty(message = "User Name is obligatory")
  @Size(min=5, message = "Too short. Minimum length is 5 characters")
  private StringProperty userName;
  @NotEmpty(message = "Password is obligatory")
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

  @Override
  public void validateModel() throws ValidationException {
    super.validate();
  }

  

  
  
}
