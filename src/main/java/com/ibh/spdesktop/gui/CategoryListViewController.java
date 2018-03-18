/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ibh.spdesktop.gui;

import com.ibh.spdesktop.bl.BusinessLogic;
import com.ibh.spdesktop.dal.Category;
import com.ibh.spdesktop.message.CrudMessage;
import com.ibh.spdesktop.message.MessageService;
import com.ibh.spdesktop.message.RefreshDataMessage;
import com.ibh.spdesktop.viewmodel.CategoryVM;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.layout.VBox;

/**
 * FXML Controller class
 *
 * @author ihorvath
 */
public class CategoryListViewController extends BaseController<CategoryVM> implements Initializable {

	@FXML
	private TableView<CategoryVM> categoryTable;
	@FXML
	private TableColumn<CategoryVM, String> nameColumn;

	@FXML
	private VBox crudContainer;

	private ObservableList<CategoryVM> data;
	private FilteredList<CategoryVM> filteredData;
	private CategoryVM currentData = null;

	public CategoryListViewController(BusinessLogic bl) {
		super(bl);
	}

	/**
	 * Initializes the controller class.
	 */
	@Override
	public void initialize(URL url, ResourceBundle rb) {

		MessageService.register(RefreshDataMessage.class, (arg) -> {
			reloadData();
		});

		nameColumn.setCellValueFactory(cellData -> cellData.getValue().getName());

		categoryTable.getSelectionModel().selectedItemProperty().addListener(((observable, oldvalue, newvalue) -> {
			currentData = newvalue;
			showDetails();
		}));

		categoryTable.setRowFactory(tv -> new TableRow<CategoryVM>() {
			@Override
			public void updateItem(CategoryVM item, boolean empty) {
				super.updateItem(item, empty);
				if (item == null) {
					setStyle("");
				} else {
					setStyle(String.format("-fx-background-color: %s;", item.getCSSColor()));
				}
			}
		});

		reloadData();

	}

	private void reloadData() {
		List<CategoryVM> cvmlist = new ArrayList<>();
		getBl().getCategRepos().getList().forEach(c -> cvmlist.add(from(c)));

		data = FXCollections.observableArrayList(cvmlist);

		filteredData = new FilteredList<>(data, p -> true);

		SortedList<CategoryVM> sortedData = new SortedList<>(filteredData);
		sortedData.comparatorProperty().bind(categoryTable.comparatorProperty());

		categoryTable.setItems(sortedData);
		if (!sortedData.isEmpty()) {
			currentData = sortedData.get(0);
		}

		showDetails();

	}

	private CategoryVM from(Category c) {
		return new CategoryVM(c.getId(), c.getName(), c.getColor());
	}

	private void showDetails() {
		if (currentData != null) {
			setUIContent(crudContainer,
					new CrudMessage(ViewEnum.CategoryCRUDView, currentData.getId().get(), CRUDEnum.View));
		}
	}

	@FXML
	public void handleNew() {
		setUIContent(crudContainer,
				new CrudMessage(ViewEnum.CategoryCRUDView, currentData.getId().get(), CRUDEnum.New));
	}

	@FXML
	public void handleDelete() {
		setUIContent(crudContainer,
				new CrudMessage(ViewEnum.CategoryCRUDView, currentData.getId().get(), CRUDEnum.Delete));
	}

	@Override
	public void setUpValidators() {

	}
}
