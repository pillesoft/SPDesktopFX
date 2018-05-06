package com.ibh.spdesktop.gui;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ibh.spdesktop.bl.BusinessLogic;
import com.ibh.spdesktop.dal.AuthInfo;
import com.ibh.spdesktop.dal.Authentication;
import com.ibh.spdesktop.message.CrudMessage;
import com.ibh.spdesktop.message.MessageService;
import com.ibh.spdesktop.message.RefreshDataMessage;
import com.ibh.spdesktop.message.UIContentMessage;
import com.ibh.spdesktop.viewmodel.AuthLimitedVM;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TitledPane;
import javafx.util.Duration;

/**
 * FXML Controller class
 *
 * @author ihorvath
 */
public class AuthViewViewController extends BaseController<AuthLimitedVM> implements Initializable {

	private static final Logger LOG = LoggerFactory.getLogger(AuthViewViewController.class);
	private AuthLimitedVM vm;

	private final String ShowAuthPaneTitle = "Show Authentication";
	private final Integer TIMERVALUE = 10;
	private Integer timerCountDown = TIMERVALUE;
	private Timeline timeline;
	
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
	private TitledPane tPaneShowAuth;
	@FXML
	private Button cmdEdit;
	@FXML
	private Button cmdDelete;

	@FXML
	private TextField txtShowUserName;

	@FXML
	private TextField txtShowPassword;

	@FXML
	public void handleEdit() {
		MessageService.send(UIContentMessage.class, new UIContentMessage(ViewEnum.AuthCRUDView, vm.getId().get(), CRUDEnum.Update));
	}

	@FXML
	public void handleCopyUserName() {

	}

	@FXML
	public void handleCopyPassword() {

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
		
		tPaneShowAuth.setExpanded(false);
		tPaneShowAuth.expandedProperty().addListener((obs, wasExpanded, isExpanded)->{
			if(isExpanded) {
				AuthInfo ai = getBl().getAuthRepos().getAuthInfo(msg.getId());
				txtShowUserName.setText(ai.getUsername());
				txtShowPassword.setText(ai.getPwdClear());
				
				// add the timer
				timerCountDown = TIMERVALUE;
				timeline = new Timeline();
		        timeline.setCycleCount(Timeline.INDEFINITE);
		        timeline.getKeyFrames().add(
		                new KeyFrame(Duration.seconds(1),
		                  new EventHandler<ActionEvent>() {
		                    // KeyFrame event handler
		                    public void handle(ActionEvent event) {
		                        timerCountDown--;
		                        // update header
		                        tPaneShowAuth.setText(String.format("%s - %s", ShowAuthPaneTitle, timerCountDown));
		                        if (timerCountDown <= 0) {
		                            timeline.stop();
		                            tPaneShowAuth.setExpanded(false);
		                            tPaneShowAuth.setText(ShowAuthPaneTitle);
		                        }
		                      }
		                }));
		        timeline.playFromStart();
			} else {
				// this is when the pane is closed by the user
				if(timeline != null) {
					timeline.stop();
					tPaneShowAuth.setText(ShowAuthPaneTitle);
				}
			}
		});
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
