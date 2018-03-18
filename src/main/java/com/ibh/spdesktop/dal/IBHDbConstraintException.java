/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ibh.spdesktop.dal;

/**
 *
 * @author ihorvath
 */
public class IBHDbConstraintException extends RuntimeException {
  /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
private final String constraintName;
  private final String tableName;
  private final String fieldName;

  public IBHDbConstraintException(String constraintName, String tableName, String fieldName) {
    super();
    this.constraintName = constraintName;
    this.tableName = tableName;
    this.fieldName = fieldName;
  }

  public String getConstraintName() {
    return constraintName;
  }

  public String getTableName() {
    return tableName;
  }

  public String getFieldName() {
    return fieldName;
  }
  
}
