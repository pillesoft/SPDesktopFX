package com.ibh.spdesktop.viewmodel;

import com.ibh.spdesktop.validation.ValidationException;
import java.time.LocalDate;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleIntegerProperty;
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

	private final String origPassword;
	
	private IntegerProperty id;
	
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
		id = new SimpleIntegerProperty(null, "id", 0);
		title = new SimpleStringProperty(null, "title", "");
		category = new SimpleStringProperty(null, "category");
		userName = new SimpleStringProperty(null, "userName", "");
		password = new SimpleStringProperty(null, "password", "");
		webUrl = new SimpleStringProperty(null, "webUrl", "");
		description = new SimpleStringProperty(null, "description", "");
		validFrom = new SimpleObjectProperty<>(null, "validFrom", LocalDate.now());

		this.origPassword = "";
	}

	public AuthenticationVM(int id, String title, String category, String userName, String password, String webUrl,
			String description, LocalDate validFrom) {
		this.id = new SimpleIntegerProperty(null, "id", id);
		this.title = new SimpleStringProperty(null, "title", title);
		this.category = new SimpleStringProperty(null, "category", category);
		this.userName = new SimpleStringProperty(null, "userName", userName);
		this.password = new SimpleStringProperty(null, "password", password);
		this.webUrl = new SimpleStringProperty(null, "webUrl", webUrl);
		this.description = new SimpleStringProperty(null, "description", description);
		this.validFrom = new SimpleObjectProperty<>(null, "validFrom", validFrom);
		
		this.origPassword = password;
	}

	@Override
	public void validateModel() throws ValidationException {
		super.validate();
	}

	
	public IntegerProperty getId() {
		return id;
	}

	public void setId(IntegerProperty id) {
		this.id = id;
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

	public String getOrigPassword() {
		return origPassword;
	}

	public boolean isPwdChanged() {
		return !origPassword.equals(this.password.get());
	}

	

}
