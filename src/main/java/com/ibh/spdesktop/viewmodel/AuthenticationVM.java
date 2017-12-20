package com.ibh.spdesktop.viewmodel;

import com.ibh.spdesktop.validation.ValidationException;
import java.time.LocalDate;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author ihorvath
 */
public class AuthenticationVM extends BaseViewModel<AuthenticationVM> {

  @NotNull
  @Size(min = 5, max = 100)
  private StringProperty title;

  @NotNull
  @Size(min = 3)
  private StringProperty category;

  @NotNull
  @Size(min = 4, max = 100)
  private StringProperty userName;

  @Size(max = 100)
  private StringProperty password;

  @Size(max = 200)
  private StringProperty webUrl;

  @Size(max = 500)
  private StringProperty description;

  private ObjectProperty<LocalDate> validFrom;

  public AuthenticationVM() {
    title = new SimpleStringProperty(null, "title", "");
    category = new SimpleStringProperty(null, "category");
    userName = new SimpleStringProperty(null, "userName", "");
    password = new SimpleStringProperty(null, "password", "");
    webUrl = new SimpleStringProperty(null, "webUrl", "");
    description = new SimpleStringProperty(null, "description", "");
    validFrom = new SimpleObjectProperty<>(null, "validFrom", LocalDate.now());
  }

  @Override
  public void validateModel() throws ValidationException {
    super.validate();
  }

  public StringProperty getTitle() {
    return title;
  }

  public void setTitle(StringProperty title) {
    this.title = title;
  }

  public StringProperty getCategory() {
    return category;
  }

  public void setCategory(StringProperty category) {
    this.category = category;
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

  public StringProperty getWebUrl() {
    return webUrl;
  }

  public void setWebUrl(StringProperty webUrl) {
    this.webUrl = webUrl;
  }

  public StringProperty getDescription() {
    return description;
  }

  public void setDescription(StringProperty description) {
    this.description = description;
  }

  public ObjectProperty<LocalDate> getValidFrom() {
    return validFrom;
  }

  public void setValidFrom(ObjectProperty<LocalDate> validFrom) {
    this.validFrom = validFrom;
  }

}
