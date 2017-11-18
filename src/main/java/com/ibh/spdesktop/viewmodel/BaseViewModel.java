package com.ibh.spdesktop.viewmodel;

import com.ibh.spdesktop.validation.VMValidation;
import com.ibh.spdesktop.validation.ValidationException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import org.hibernate.validator.internal.engine.path.PathImpl;

/**
 *
 * @author ihorvath
 */
public abstract class BaseViewModel<T> implements VMValidation {

  private HashMap<String, List<String>> validationErrors;

  public BaseViewModel() {
    validationErrors = new HashMap<>();
  }

  
  @Override
  public HashMap<String, List<String>> getValidationErrors() {
    return validationErrors;
  }

  @Override
  public boolean isValid() {
    return validationErrors.isEmpty();
  }

  protected void validate() throws ValidationException {
    validationErrors = new HashMap<>();
    
    ValidatorFactory valfact = Validation.buildDefaultValidatorFactory();
    Validator validator = valfact.getValidator();
    Set<ConstraintViolation<BaseViewModel<T>>> errors = validator.validate(this);

    convertErrors(errors);
    if (!validationErrors.isEmpty()) {
      throw new ValidationException(validationErrors);
    }
  }

  private void convertErrors(Set<ConstraintViolation<BaseViewModel<T>>> errors) {
    errors.stream().forEach((err) -> {
      String propname = ((PathImpl) err.getPropertyPath()).getLeafNode().getName();
      if (validationErrors.containsKey(propname)) {
        validationErrors.get(propname).add(err.getMessage());
      } else {
        List<String> v = new ArrayList<>();
        v.add(err.getMessage());
        validationErrors.put(propname, v);
      }
    });
  }

}
