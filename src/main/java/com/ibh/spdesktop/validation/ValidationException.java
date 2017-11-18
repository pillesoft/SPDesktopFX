package com.ibh.spdesktop.validation;

import java.util.HashMap;
import java.util.List;

/**
 *
 * @author ihorvath
 */
public class ValidationException extends RuntimeException {

  private static final long serialVersionUID = 1L;
  
  private final HashMap<String, List<String>> validationError;

  public ValidationException(HashMap<String, List<String>> validationError) {
    this.validationError = validationError;
  }

  public ValidationException(HashMap<String, List<String>> validationError, String string) {
    super(string);
    this.validationError = validationError;
  }

  public ValidationException(HashMap<String, List<String>> validationError, String string, Throwable thrwbl) {
    super(string, thrwbl);
    this.validationError = validationError;
  }

  public ValidationException(HashMap<String, List<String>> validationError, Throwable thrwbl) {
    super(thrwbl);
    this.validationError = validationError;
  }

  public HashMap<String, List<String>> getValidationError() {
    return validationError;
  }



}
