/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ibh.spdesktop;

import com.ibh.spdesktop.bl.BusinessLogic;
import com.ibh.spdesktop.gui.BaseController;
import com.ibh.spdesktop.gui.View;
import com.ibh.spdesktop.gui.ViewEnum;
import com.ibh.spdesktop.gui.ViewFactory;
import java.util.Locale;
import java.util.ResourceBundle;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

/**
 *
 * @author ihorvath
 */
public class SPDesktop extends Application {
  private BusinessLogic bl;
  
  @Override
  public void start(Stage primaryStage) throws Exception {

    Application.setUserAgentStylesheet(STYLESHEET_CASPIAN);
    
    setBl(new BusinessLogic());

    ResourceBundle bundle = ResourceBundle.getBundle("bundles.UIBundle", new Locale("hu"));
    View v = ViewFactory.getViews().get(ViewEnum.Main);
    
    FXMLLoader loader = new FXMLLoader(getClass().getResource(v.getFxmlPath()), bundle);
    BaseController controller = (BaseController)v.getController().getDeclaredConstructor(BusinessLogic.class).newInstance(getBl());
    loader.setController(controller);
    Parent root = loader.load();

    Scene scene = new Scene(root);
    scene.getStylesheets().add(this.getClass().getResource("/styles/Application.css").toString());

    // Add a custom icon.
    primaryStage.getIcons().add(new Image(this.getClass().getResource("/image/Safe-icon.png").toString()));

    primaryStage.setTitle(bundle.getString("AppTitle"));
    primaryStage.setScene(scene);
    primaryStage.show();
  }

  /**
   * @param args the command line arguments
   */
  public static void main(String[] args) {
    launch(args);
  }

  public BusinessLogic getBl() {
    return bl;
  }

  public void setBl(BusinessLogic bl) {
    this.bl = bl;
  }

  

}
