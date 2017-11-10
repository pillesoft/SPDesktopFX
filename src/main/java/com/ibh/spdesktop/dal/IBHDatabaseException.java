
package com.ibh.spdesktop.dal;

/**
 *
 * @author ihorvath
 */
public class IBHDatabaseException extends RuntimeException {
  private final String status;

  public static final String AVAILABLE = "AVAILABLE";
  public static final String NOTAVAILABLE = "NOTAVAILABLE";

  public String getStatus() {
    return status;
  }
  
  public IBHDatabaseException(String status) {
    super();
    this.status = status;
  }
  
  
}
