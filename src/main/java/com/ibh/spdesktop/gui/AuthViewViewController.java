package com.ibh.spdesktop.gui;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ibh.spdesktop.bl.BusinessLogic;
import com.ibh.spdesktop.dal.Authentication;
import com.ibh.spdesktop.message.CrudMessage;
import com.ibh.spdesktop.message.MessageService;
import com.ibh.spdesktop.message.RefreshDataMessage;
import com.ibh.spdesktop.message.UIContentMessage;
import com.ibh.spdesktop.viewmodel.AuthLimitedVM;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

/**
 * FXML Controller class
 *
 * @author ihorvath
 */
public class AuthViewViewController extends BaseController<AuthLimitedVM> implements Initializable {

	private static final Logger LOG = LoggerFactory.getLogger(AuthViewViewController.class);
	private AuthLimitedVM vm;

	@FXML
	private TextField txtTitle;
	@FXML
	private TextField txtCategory;
	@FXML
	private Hyperlink txtWebAddress;
	@FXML
	private TextField txtValidFrom;
	@FXML
	private TextArea txaDescription;
	@FXML
	private Button cmdShowAuth;
	@FXML
	private Button cmdEdit;
	@FXML
	private Button cmdDelete;

	@FXML
	public void handleEdit() {
		MessageService.send(UIContentMessage.class, new UIContentMessage(ViewEnum.AuthCRUDView, vm.getId().get(), CRUDEnum.Update));
	}

	@FXML
	public void handleShowAuth() {

	}

	@FXML
	public void handleDelete() {

		getBl().getAuthRepos().delete(vm.getId().get());

		MessageService.send(RefreshDataMessage.class, new RefreshDataMessage(ViewEnum.AuthListView));

	}

	@FXML
	public void handleWebAddressLink() {
		String command = String.format("start %s %s", "firefox", txtWebAddress.getText());
		try {
			Runtime.getRuntime().exec(new String[] { "cmd", "/c", command });
			// this is linux
			// Runtime.getRuntime().exec(new String[] { "chromium-browser",
			// "http://example.com/" });
		} catch (IOException ex) {
			LOG.warn("cannot open browser", ex);
		}
	}

	public AuthViewViewController(BusinessLogic bl) {
		super(bl);
	}

	/**
	 * Initializes the controller class.
	 */
	@Override
	public void initialize(URL url, ResourceBundle rb) {
		// TODO

		CrudMessage msg = (CrudMessage) getMessage();
		switch (msg.getCrud()) {
		case View:
			vm = fromEntityToVM(getBl().getAuthRepos().getById(msg.getId()));
			break;
		default:
			break;
		}

		setUpValidators();
	}

	@Override
	public void setUpValidators() {
		try {
			setUpValidator(txtTitle, vm.getTitle());
			setUpValidator(txtCategory, vm.getCategory());
			setUpValidator(txtWebAddress, vm.getWebUrl());
			setUpValidator(txtValidFrom, vm.getValidFrom());
			setUpValidator(txaDescription, vm.getDescription());

			setControlStateNormal();
		} catch (Exception ex) {
			LOG.error(ex.getMessage(), ex);
		}
	}

	private AuthLimitedVM fromEntityToVM(Authentication c) {
		return new AuthLimitedVM(c.getId(), c.getTitle(), c.getCategory().getName(), c.getWeburl(), c.getDescription(), c.getValidfrom(), c.getCategory().getColor());
	}

}
