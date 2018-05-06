/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ibh.spdesktop.gui;

import java.net.URL;
import java.util.ResourceBundle;

import org.apache.commons.lang3.StringUtils;

import com.ibh.spdesktop.bl.BusinessLogic;
import com.ibh.spdesktop.message.CrudMessage;
import com.ibh.spdesktop.message.MessageService;
import com.ibh.spdesktop.message.RefreshDataMessage;
import com.ibh.spdesktop.message.UIContentMessage;
import com.ibh.spdesktop.viewmodel.AuthLimitedVM;

import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

/**
 * FXML Controller class
 *
 * @author ihorvath
 */
public class AuthListViewController extends BaseController<AuthLimitedVM> implements Initializable {

	@FXML
	private TableView<AuthLimitedVM> authTable;
	@FXML
	private TableColumn<AuthLimitedVM, String> categoryColumn;
	@FXML
	private TableColumn<AuthLimitedVM, String> titleColumn;
	@FXML
	private TableColumn<AuthLimitedVM, Integer> howOldColumn;

	@FXML
	private TextField categoryFilter;
	@FXML
	private TextField titleFilter;

	@FXML
	private VBox crudContainer;

	private ObservableList<AuthLimitedVM> data;
	private FilteredList<AuthLimitedVM> filteredData;
	private AuthLimitedVM currentData = null;

	public AuthListViewController(BusinessLogic bl) {
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
		MessageService.register(UIContentMessage.class, (arg) -> {
			setContent((UIContentMessage)arg);
		});

		categoryColumn.setCellValueFactory(cellData -> cellData.getValue().getCategory());
		titleColumn.setCellValueFactory(cellData -> cellData.getValue().getTitle());
		howOldColumn.setCellValueFactory(cellData -> cellData.getValue().getNumberOfDays().asObject());
//		howOldColumn.setCellValueFactory(new PropertyValueFactory<AuthLimitedVM, Integer>("numberOfDays"));
//		howOldColumn
//				.setCellFactory(new Callback<TableColumn<AuthLimitedVM, Integer>, TableCell<AuthLimitedVM, Integer>>() {
//
//					@Override
//					public TableCell<AuthLimitedVM, Integer> call(TableColumn<AuthLimitedVM, Integer> param) {
//						return new TableCell<AuthLimitedVM, Integer>() {
//							@Override
//							protected void updateItem(Integer howold, boolean empty) {
//								super.updateItem(howold, empty);
//								if (empty) {
//									setText(null);
//								} else {
//									setText(Integer.toString(howold));
//								}
//							}
//						};
//					}
//				});
		// howOldColumn.setCellFactory(new Callback<TableColumn<AuthLimitedVM, Integer>,
		// TableCell<AuthLimitedVM, Integer>>() {
		// @Override
		// public TableCell<AuthLimitedVM, Integer> call(TableColumn<AuthLimitedVM,
		// Integer> col) {
		// return new TableCell<AuthLimitedVM, Integer>() {
		// @Override
		// protected void updateItem(Integer howold, boolean empty) {
		// super.updateItem(howold, empty);
		// if (empty) {
		// setText(null);
		// } else {
		// setText(Integer.toString(howold));
		// }
		// }
		// };
		// }
		// });

		authTable.getSelectionModel().selectedItemProperty().addListener(((observable, oldvalue, newvalue) -> {
			currentData = newvalue;
			showDetails();
		}));

		categoryFilter.textProperty().addListener((observable, oldValue, newValue) -> {
			doFilter();
		});

		titleFilter.textProperty().addListener((obs, oldv, newv) -> {
			doFilter();
		});

		authTable.setRowFactory(tv -> new TableRow<AuthLimitedVM>() {
			@Override
			public void updateItem(AuthLimitedVM item, boolean empty) {
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

		data = getBl().getAuthRepos().getAuthLimited();

		filteredData = new FilteredList<>(data, a -> {
			String cfilter = categoryFilter.getText();
			String tfilter = titleFilter.getText();
			
			String lowercFilter = cfilter.toLowerCase();
			String lowertFilter = tfilter.toLowerCase();
			if(StringUtils.isEmpty(cfilter) && StringUtils.isEmpty(tfilter)) {
				//both are empty
				return true;
			} else if (((cfilter != null && !cfilter.isEmpty())
					&& a.getCategory().getValue().toLowerCase().contains(lowercFilter))
					&& ((tfilter != null && !tfilter.isEmpty())
							&& a.getTitle().getValue().toLowerCase().contains(lowertFilter))) {
				// none of empty
				return true;
			} else if (((cfilter != null && !cfilter.isEmpty())
					&& a.getCategory().getValue().toLowerCase().contains(lowercFilter))
					&& (tfilter == null || tfilter.isEmpty())) {
				// title is empty
				return true;
			} else if ((cfilter == null || cfilter.isEmpty()) && ((tfilter != null && !tfilter.isEmpty())
					&& a.getTitle().getValue().toLowerCase().contains(lowertFilter))) {
				// category is empty
				return true;
			}
			return false;
			
		});

		SortedList<AuthLimitedVM> sortedData = new SortedList<>(filteredData);
		sortedData.comparatorProperty().bind(authTable.comparatorProperty());

		authTable.setItems(sortedData);
		if (!sortedData.isEmpty()) {
			currentData = sortedData.get(0);
		}

		showDetails();

	}

	private void showDetails() {

		if (currentData != null) {
			setUIContent(crudContainer,
					new CrudMessage(ViewEnum.AuthViewView, currentData.getId().get(), CRUDEnum.View));
		}
	}

	@FXML
	public void handleNew() {
		setUIContent(crudContainer,
				new CrudMessage(ViewEnum.AuthCRUDView, 0, CRUDEnum.New));
	}

	private void setContent(UIContentMessage msg) {
		setUIContent(crudContainer,
				new CrudMessage(msg.getContent(), msg.getId(), msg.getCrud()));		
	}

	@FXML
	public void handleClearFilter() {
		categoryFilter.clear();
		titleFilter.clear();
		filteredData.setPredicate(a -> true);
	}

	private void doFilter() {
		String cfilter = categoryFilter.getText();
		String tfilter = titleFilter.getText();

		filteredData.setPredicate(a -> {

			String lowercFilter = cfilter.toLowerCase();
			String lowertFilter = tfilter.toLowerCase();
			// none of empty
			if (((cfilter != null && !cfilter.isEmpty())
					&& a.getCategory().getValue().toLowerCase().contains(lowercFilter))
					&& ((tfilter != null && !tfilter.isEmpty())
							&& a.getTitle().getValue().toLowerCase().contains(lowertFilter))) {
				return true;
			} else if (((cfilter != null && !cfilter.isEmpty())
					&& a.getCategory().getValue().toLowerCase().contains(lowercFilter))
					&& (tfilter == null || tfilter.isEmpty())) {
				// title is empty
				return true;
			} else if ((cfilter == null || cfilter.isEmpty()) && ((tfilter != null && !tfilter.isEmpty())
					&& a.getTitle().getValue().toLowerCase().contains(lowertFilter))) {
				// category is empty
				return true;
			}
			return false;
		});

	}

	@Override
	public void setUpValidators() {

	}
}
