package com.ibh.spdesktop.validation;

import java.util.HashMap;
import java.util.List;

/**
 *
 * @author ihorvath
 */
public interface VMValidation {
  void validateModel() throws ValidationException;
  boolean isValid();
  HashMap<String, List<String>> getValidationErrors();
}
