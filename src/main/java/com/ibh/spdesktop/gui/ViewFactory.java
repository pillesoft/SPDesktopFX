/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ibh.spdesktop.gui;

import java.util.EnumMap;

/**
 *
 * @author ihorvath
 */
public final class ViewFactory {

  private ViewFactory() { }
  
  public static EnumMap<ViewEnum, View> getViews() {
    EnumMap<ViewEnum, View> view = new EnumMap<>(ViewEnum.class);
    
    view.put(ViewEnum.Main, new View("Main.fxml", MainController.class));
    view.put(ViewEnum.Login, new View("Login.fxml", LoginController.class));
    view.put(ViewEnum.AuthListView, new View("AuthListView.fxml", AuthListViewController.class));
    view.put(ViewEnum.AuthViewView, new View("AuthViewView.fxml", AuthViewViewController.class));
    view.put(ViewEnum.AuthCRUDView, new View("AuthCRUDView.fxml", AuthCRUDViewController.class));
    view.put(ViewEnum.CategoryListView, new View("CategoryListView.fxml", CategoryListViewController.class));
    view.put(ViewEnum.CategoryCRUDView, new View("CategoryCRUDView.fxml", CategoryCRUDViewController.class));

    return view;
  }
}
