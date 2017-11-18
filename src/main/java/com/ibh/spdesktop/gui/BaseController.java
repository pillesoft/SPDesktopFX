package com.ibh.spdesktop.gui;

import com.ibh.spdesktop.bl.BusinessLogic;
import com.ibh.spdesktop.message.BaseMessage;
import com.ibh.spdesktop.validation.ValidationException;
import com.ibh.spdesktop.viewmodel.BaseViewModel;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;
import javafx.beans.property.Property;
import javafx.scene.control.Control;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.CornerRadii;
import javafx.scene.paint.Paint;

/**
 *
 * @author ihorvath
 */
public abstract class BaseController<T extends BaseViewModel> implements IController {

  private final BusinessLogic bl;
  private ResourceBundle bundle;
  private BaseMessage message;
  private final Map<String, Control> validatedControls;
//  private T instance;

  protected BusinessLogic getBl() {
    return bl;
  }

  protected ResourceBundle getBundle() {
    return bundle;
  }

  protected void setBundle(ResourceBundle bundle) {
    this.bundle = bundle;
  }

  protected void setMessage(BaseMessage message) {
    this.message = message;
  }

//  public void setInstance(T instance) {
//    this.instance = instance;
//  }

  public BaseController(BusinessLogic bl) {
    this.bl = bl;
    validatedControls = new HashMap<>();
  }
  
  protected void setUpValidator(Control ctrl, Property field) throws Exception {

    if (ctrl instanceof TextField) {
      TextField ctrltxt = (TextField) ctrl;
      ctrltxt.textProperty().bindBidirectional(field);
      validatedControls.put(field.getName(), ctrltxt);
    } else {
      throw new Exception("not defined control");
    }

  }

  protected void setControlStateNormal() {
    BorderStroke bs = new BorderStroke(Paint.valueOf("GREY"), BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT);
    Border b = new Border(bs);
    Tooltip tt = new Tooltip("");

    validatedControls.forEach((k, v) -> {
      v.borderProperty().set(b);
      v.tooltipProperty().set(tt);
    });
  }

  protected void setControlStateError(ValidationException valexc) {
    BorderWidths width = new BorderWidths(2);
    BorderStroke bs = new BorderStroke(Paint.valueOf("RED"), BorderStrokeStyle.SOLID, CornerRadii.EMPTY, width);
    Border b = new Border(bs);

    valexc.getValidationError().forEach((field, errs) -> {
      Control ctrl = validatedControls.get(field);
      ctrl.borderProperty().set(b);

      Tooltip tt = new Tooltip(String.join("\n", errs));
      ctrl.tooltipProperty().set(tt);

    });

  }

}
