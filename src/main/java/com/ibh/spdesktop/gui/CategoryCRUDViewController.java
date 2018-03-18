package com.ibh.spdesktop.gui;

import com.ibh.spdesktop.bl.BusinessLogic;
import com.ibh.spdesktop.dal.Category;
import com.ibh.spdesktop.message.ActionMessage;
import com.ibh.spdesktop.message.CrudMessage;
import com.ibh.spdesktop.message.BaseMessage;
import com.ibh.spdesktop.message.MessageService;
import com.ibh.spdesktop.message.RefreshDataMessage;
import com.ibh.spdesktop.validation.ValidationException;
import com.ibh.spdesktop.viewmodel.CategoryVM;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * FXML Controller class
 *
 * @author ihorvath
 */
public class CategoryCRUDViewController extends BaseController<CategoryVM> implements Initializable {

	private static final Logger LOG = LoggerFactory.getLogger(CategoryCRUDViewController.class);
	private Category instance;
	private CategoryVM vm;

	@FXML
	private Button cmdSave;
	@FXML
	private TextField txtName;
	@FXML
	private ColorPicker cpColor;

	@FXML
	public void handleSave() {

		setControlStateNormal();

		try {
			vm.validateModel();

			CrudMessage msg = (CrudMessage) getMessage();
			switch (msg.getCrud()) {
			case New:
				instance = fromVMToEntity();
				getBl().getCategRepos().add(instance);
				break;
			case View:
			case Update:
				instance = fromVMToEntity();
				getBl().getCategRepos().update(instance);
				break;
			case Delete:
				instance = fromVMToEntity();
				getBl().getCategRepos().delete(instance.getId());
				break;
			default:
				throw new AssertionError(msg.getCrud().name());

			}

			MessageService.send(RefreshDataMessage.class, new RefreshDataMessage(ViewEnum.CategoryListView));
		} catch (ValidationException exc) {
			setControlStateError(exc);
		}

	}

	@FXML
	public void handleCancel() {
		MessageService.send(RefreshDataMessage.class, new RefreshDataMessage(ViewEnum.CategoryListView));
	}

	public CategoryCRUDViewController(BusinessLogic bl) {
		super(bl);
	}

	/**
	 * Initializes the controller class.
	 */
	@Override
	public void initialize(URL url, ResourceBundle rb) {
		// the message type is CrudMessage
		CrudMessage msg = (CrudMessage) getMessage();
		switch (msg.getCrud()) {
		case View:
		case Update:
			vm = fromEntityToVM(getBl().getCategRepos().getById(msg.getId()));
			break;
		case New:
			vm = new CategoryVM();
			break;
		case Delete:
			vm = fromEntityToVM(getBl().getCategRepos().getById(msg.getId()));
			txtName.setEditable(false);
			cpColor.setDisable(true);
			cmdSave.setText("Delete");
			break;
		default:
			break;
		}

		setUpValidators();
	}

	public Category getInstance() {
		return instance;
	}

	public void setInstance(Category instance) {
		this.instance = instance;
	}

	@Override
	public void setUpValidators() {
		try {
			setUpValidator(txtName, vm.getName());

			cpColor.setValue(vm.getRGBColor());

			setControlStateNormal();
		} catch (Exception ex) {
			LOG.error(ex.getMessage(), ex);
		}
	}

	private CategoryVM fromEntityToVM(Category c) {
		return new CategoryVM(c.getId(), c.getName(), c.getColor());
	}

	private Category fromVMToEntity() {
		Color c = cpColor.getValue();
		return new Category(vm.getId().get(), vm.getName().get(), c.toString());
	}

}
