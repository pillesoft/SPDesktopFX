
package com.ibh.spdesktop.dal;

/**
 *
 * @author ihorvath
 */
public class IBHDatabaseException extends RuntimeException {

  /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
private final DBException type;
  
  public enum DBException {
    AVAILABLE("The database is available"),
    NOTAVAILABLE("The database is not available");
    
    private String description;

    DBException(String description) {
      this.description = description;
    }

    public String getDescription() {
      return description;
    }
    
  }
  
  public IBHDatabaseException(DBException type) {
    super();
    this.type = type;
  }

  public DBException getType() {
    return type;
  }
  
}
