
package com.ibh.spdesktop.gui;

/**
 *
 * @author ihorvath
 */
public class View {
  private final String fxmlPath;
  private final Class<?> controller;

  public String getFxmlPath() {
    return String.format("/fxml/%s", fxmlPath);
  }

  public Class<?> getController() {
    return controller;
  }

  public View(String fxmlPath, Class<?> controller) {
    this.fxmlPath = fxmlPath;
    this.controller = controller;
  }

}
