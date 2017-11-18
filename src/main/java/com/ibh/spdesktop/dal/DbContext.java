/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ibh.spdesktop.dal;

import com.ibh.spdesktop.gui.LoginController;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import org.h2.jdbc.JdbcSQLException;
import org.slf4j.LoggerFactory;
//import org.hibernate.SessionFactory;
//import org.hibernate.boot.MetadataSources;
//import org.hibernate.boot.registry.StandardServiceRegistry;
//import org.hibernate.boot.registry.StandardServiceRegistryBuilder;

/**
 *
 * @author ihorvath
 */
public final class DbContext {

  private static final org.slf4j.Logger LOG = LoggerFactory.getLogger(DbContext.class);

  private DbContext() { }

  
  //private static EntityManager _EntMgr = null;
  private static EntityManagerFactory emFactory = null;
  
  private static String connString = "jdbc:h2:%s;IFEXISTS=TRUE;CIPHER=AES";

  public static void createDatabase(String dbName, char[] pwd, char[] encrpwd)  throws JdbcSQLException, IBHDatabaseException {
    if (dbName.isEmpty()) {
      throw new IllegalArgumentException("DbName is empty");
    }
    if (Files.exists(getDbFileName(dbName))) {
      throw new IBHDatabaseException(IBHDatabaseException.AVAILABLE);
    }

    HashMap<String, String> sett = new HashMap<String, String>();
    sett.put("javax.persistence.jdbc.url", String.format("jdbc:h2:%s;CIPHER=AES", getDbPath(dbName).toString()));
    sett.put("javax.persistence.jdbc.user", dbName);
    sett.put("javax.persistence.jdbc.password", String.format("%s %s", String.valueOf(encrpwd), String.valueOf(pwd)));
    sett.put("javax.persistence.schema-generation.database.action", "create");
//    sett.put("eclipselink.ddl-generation", "create-or-extend-tables");
//    sett.put("eclipselink.ddl-generation.output-mode", "database");

    init(sett);

  }

  public static void connect(String dbName, char[] pwd, char[] encrpwd) throws JdbcSQLException, IBHDatabaseException {
    if (dbName.isEmpty()) {
      throw new IllegalArgumentException("DbName is empty");
    }
    if (!Files.exists(getDbFileName(dbName))) {
      throw new IBHDatabaseException(IBHDatabaseException.NOTAVAILABLE);
    }

    Map sett = new HashMap<String, String>();

    sett.put("javax.persistence.jdbc.url", String.format(connString, getDbPath(dbName).toString()));
    sett.put("javax.persistence.jdbc.user", dbName);
    sett.put("javax.persistence.jdbc.password", String.format("%s %s", String.valueOf(encrpwd), String.valueOf(pwd)));

    init(sett);
    
    // try to get Entity Manager to check if we could really connect to the database
    getEM();

  }

  private static Path getDbPath(String dbName) {

    Properties props = new Properties();
    try {
      props.load(DbContext.class.getResourceAsStream("/config.properties"));
    } catch (IOException ex) {
      LOG.error("cannot load properties file", ex);
    }
    
//    String appdatapath = System.getenv("APPDATA");
//    Path dbpath = Paths.get(appdatapath, "IBHSP", DbName);
    Path dbpath = Paths.get(props.getProperty("DBFolder"), dbName);
    
    return dbpath;
  }

  private static Path getDbFileName(String dbName) {
    return getDbPath(dbName.concat(".mv.db"));
  }


  private static void init(Map sett) throws JdbcSQLException {
    emFactory = Persistence.createEntityManagerFactory("SPDesktopPU", sett);
  }

  public static EntityManager getEM() {
    return emFactory.createEntityManager();
  }

}