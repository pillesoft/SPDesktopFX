package com.ibh.spdesktop.gui;

import com.ibh.spdesktop.bl.BusinessLogic;
import com.ibh.spdesktop.message.BaseMessage;
import com.ibh.spdesktop.validation.ValidationException;
import com.ibh.spdesktop.viewmodel.BaseViewModel;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;
import javafx.beans.property.Property;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.ComboBoxBase;
import javafx.scene.control.Control;
import javafx.scene.control.TextInputControl;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Paint;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author ihorvath
 */
public abstract class BaseController<T extends BaseViewModel> implements IController {

  private static final Logger LOG = LoggerFactory.getLogger(BaseController.class);

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

  protected BaseMessage getMessage() {
    return message;
  }

//  public void setInstance(T instance) {
//    this.instance = instance;
//  }
  public BaseController(BusinessLogic bl) {
    this.bl = bl;
    validatedControls = new HashMap<>();
  }

  protected void setUpValidator(Control ctrl, Property field) throws Exception {

    if (ctrl instanceof TextInputControl) {
      TextInputControl ctrltxt = (TextInputControl) ctrl;
      ctrltxt.textProperty().bindBidirectional(field);
      ctrltxt.setText(String.valueOf(field.getValue()));
      validatedControls.put(field.getName(), ctrltxt);
    } else if (ctrl instanceof ComboBoxBase) {
      ComboBoxBase ctrlcmb = (ComboBoxBase) ctrl;
      ctrlcmb.valueProperty().bindBidirectional(field);
      ctrlcmb.setValue(field.getValue());
      validatedControls.put(field.getName(), ctrlcmb);
    } else {
      throw new Exception("not defined control - " + field.getName());
    }

  }

  protected void setControlStateNormal() {
    BorderStroke bs = new BorderStroke(Paint.valueOf("GREY"), BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT);
    Border b = new Border(bs);

    validatedControls.forEach((k, v) -> {
      v.borderProperty().set(b);
      v.tooltipProperty().set(null);
    });
  }

  protected void setControlStateError(ValidationException valexc) {
    BorderWidths width = new BorderWidths(2);
    BorderStroke bs = new BorderStroke(Paint.valueOf("RED"), BorderStrokeStyle.SOLID, CornerRadii.EMPTY, width);
    Border b = new Border(bs);

    valexc.getValidationError().forEach((field, errs) -> {
      Control ctrl = validatedControls.get(field);
      if (ctrl == null) {
        LOG.warn("there is no control for field: {}", field);
      } else {
        ctrl.borderProperty().set(b);

        Tooltip tt = new Tooltip(String.join("\n", errs));
        ctrl.tooltipProperty().set(tt);
      }
    });

  }
  
  protected void setUIContent(Pane container, BaseMessage msg) {
	    Node node;
	    try {
	      View v = ViewFactory.getViews().get(msg.getContent());

	      FXMLLoader loader = new FXMLLoader(getClass().getResource(v.getFxmlPath()), getBundle());
	      BaseController<T> controller = (BaseController<T>) v.getController().getDeclaredConstructor(BusinessLogic.class).newInstance(getBl());
	      loader.setController(controller);
	      controller.setMessage(msg);

	      node = loader.load();
	      
	      container.getChildren().clear();
	      container.getChildren().add(node);

	    } catch (IOException ex) {
	    	LOG.warn(null, ex);
	    } catch (NoSuchMethodException ex) {
	    	LOG.warn(null, ex);
	    } catch (SecurityException ex) {
	    	LOG.warn(null, ex);
	    } catch (InstantiationException ex) {
	    	LOG.warn(null, ex);
	    } catch (IllegalAccessException ex) {
	    	LOG.warn(null, ex);
	    } catch (IllegalArgumentException ex) {
	    	LOG.warn(null, ex);
	    } catch (InvocationTargetException ex) {
	    	LOG.warn(null, ex);
	    }
  }

}
