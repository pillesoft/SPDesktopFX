package com.ibh.spdesktop.gui;

import com.ibh.spdesktop.bl.BusinessLogic;
import com.ibh.spdesktop.bl.Crypt;
import com.ibh.spdesktop.dal.Authentication;
import com.ibh.spdesktop.dal.Category;
import com.ibh.spdesktop.message.ActionMessage;
import com.ibh.spdesktop.message.CrudMessage;
import com.ibh.spdesktop.message.BaseMessage;
import com.ibh.spdesktop.message.MessageService;
import com.ibh.spdesktop.message.RefreshDataMessage;
import com.ibh.spdesktop.validation.ValidationException;
import com.ibh.spdesktop.viewmodel.AuthenticationVM;
import com.ibh.spdesktop.viewmodel.CategoryVM;

import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * FXML Controller class
 *
 * @author ihorvath
 */
public class AuthCRUDViewController extends BaseController<AuthenticationVM> implements Initializable {

	private static final Logger LOG = LoggerFactory.getLogger(AuthCRUDViewController.class);
	private Authentication instance;
	private AuthenticationVM vm;
	private List<Category> categlist;

	@FXML
	private TextField txtTitle;
	@FXML
	private ComboBox<String> cmbCategory;
	@FXML
	private TextField txtUserName;
	@FXML
	private TextField txtPassword;
	@FXML
	private TextField txtWebAddress;
	@FXML
	private DatePicker dpValidFrom;
	@FXML
	private TextArea txaDescription;
	@FXML
	private Button cmdSave;

	@FXML
	public void handleSave() {

		setControlStateNormal();

		try {
			vm.validateModel();

			CrudMessage msg = (CrudMessage) getMessage();
			switch (msg.getCrud()) {
			case New:
				instance = fromVMToEntity();
				getBl().getAuthRepos().add(instance);
				break;
			case View:
			case Update:
				instance = fromVMToEntity();
				getBl().getAuthRepos().update(instance);
				break;
			case Delete:
				instance = fromVMToEntity();
				getBl().getAuthRepos().delete(instance.getId());
				break;
			default:
				throw new AssertionError(msg.getCrud().name());

			}

			MessageService.send(RefreshDataMessage.class, new RefreshDataMessage(ViewEnum.AuthListView));
		} catch (ValidationException exc) {
			setControlStateError(exc);
		}

	}

	@FXML
	public void handleCancel() {
		MessageService.send(ActionMessage.class, new ActionMessage(ViewEnum.AuthListView));
	}

	public AuthCRUDViewController(BusinessLogic bl) {
		super(bl);
	}

	/**
	 * Initializes the controller class.
	 */
	@Override
	public void initialize(URL url, ResourceBundle rb) {
		// TODO
		// vm = new AuthenticationVM();
		categlist = getBl().getCategRepos().getList();
		List<String> categnamelist = categlist.stream().map(c -> c.getName()).collect(Collectors.toList());

		cmbCategory.setItems(FXCollections.observableList(categnamelist));

		CrudMessage msg = (CrudMessage) getMessage();
		switch (msg.getCrud()) {
		case View:
		case Update:
			vm = fromEntityToVM(getBl().getAuthRepos().getById(msg.getId()));
			break;
		case New:
			vm = new AuthenticationVM();
			break;
		case Delete:
			vm = fromEntityToVM(getBl().getAuthRepos().getById(msg.getId()));
			// txtName.setEditable(false);
			// cpColor.setDisable(true);
			cmdSave.setText("Delete");
			break;
		default:
			break;
		}

		setUpValidators();
	}

	public Authentication getInstance() {
		return instance;
	}

	public void setInstance(Authentication instance) {
		this.instance = instance;
	}

	@Override
	public void setUpValidators() {
		try {
			setUpValidator(txtTitle, vm.getTitle());
			setUpValidator(cmbCategory, vm.getCategory());
			setUpValidator(txtUserName, vm.getUserName());
			setUpValidator(txtPassword, vm.getPassword());
			setUpValidator(txtWebAddress, vm.getWebUrl());
			setUpValidator(dpValidFrom, vm.getValidFrom());
			setUpValidator(txaDescription, vm.getDescription());

			setControlStateNormal();
		} catch (Exception ex) {
			LOG.error(ex.getMessage(), ex);
		}
	}

	private AuthenticationVM fromEntityToVM(Authentication c) {
		return new AuthenticationVM(c.getId(), c.getTitle(), c.getCategory().getName(), c.getUsername(),
				c.getPassword(), c.getWeburl(), c.getDescription(), c.getValidfrom());
	}

	private Authentication fromVMToEntity() {
		Authentication auth = new Authentication();
		auth.setId(vm.getId().get());
		auth.setTitle(vm.getTitle().get());
		auth.setCategory(categlist.stream().filter(c->c.getName().equals(vm.getCategory().get())).findFirst().get());
		auth.setUsername(vm.getUserName().get());
		
		String cryptpwd="";
		try {
			cryptpwd = Crypt.encrypt(vm.getPassword().get().getBytes(StandardCharsets.UTF_8));
		} catch (InvalidKeyException | InvalidAlgorithmParameterException | NoSuchAlgorithmException e) {
			LOG.warn("encrypt error", e);
		}
		
		auth.setPassword(cryptpwd);
		auth.setWeburl(vm.getWebUrl().get());
		auth.setDescription(vm.getDescription().get());
		auth.setValidfrom(vm.getValidFrom().get());

		return auth;
	}

}
