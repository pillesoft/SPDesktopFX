package com.ibh.spdesktop.dal;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import org.h2.jdbc.JdbcSQLException;
import org.slf4j.Logger;
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

  private static final Logger LOG = LoggerFactory.getLogger(DbContext.class);
  private static final Properties PROPERTIES = new Properties();
  private static EntityManagerFactory emFactory = null;
  private static String connString = "jdbc:h2:%s;IFEXISTS=TRUE;CIPHER=AES";

  private DbContext() {
  }

  static {
//    if (props == null) {
    InputStream is = DbContext.class.getResourceAsStream("/config.properties");
    try {
      PROPERTIES.load(is);
    } catch (IOException ex) {
      LOG.warn("cannot load properties file", ex);
    } finally {
      try {
        is.close();
      } catch (IOException ex) {
        LOG.warn("cannot close stram");
      }
    }

    //  }
    //return props;
  }

  private static boolean isEncrypt() {
    if (PROPERTIES.containsKey("Encrypt")) {
      String isencr = PROPERTIES.getProperty("Encrypt");
      return isencr.equals("1");
    }
    return true;
  }

  public static void createDatabase(String dbName, char[] pwd, char[] encrpwd) throws JdbcSQLException, IBHDatabaseException {
    if (dbName.isEmpty()) {
      throw new IllegalArgumentException("DbName is empty");
    }
    if (Files.exists(getDbFileName(dbName))) {
      throw new IBHDatabaseException(IBHDatabaseException.DBException.AVAILABLE);
    }

    HashMap<String, String> sett = new HashMap<>();
    String url = "jdbc:h2:%s;CIPHER=AES";
    if (!isEncrypt()) {
      url = "jdbc:h2:%s";
    }
    sett.put("javax.persistence.jdbc.url", String.format(url, getDbPath(dbName).toString()));
    sett.put("javax.persistence.jdbc.user", dbName);
    if (isEncrypt()) {
      sett.put("javax.persistence.jdbc.password", String.format("%s %s", String.valueOf(encrpwd), String.valueOf(pwd)));
    } else {
      sett.put("javax.persistence.jdbc.password", String.format("%s", String.valueOf(pwd)));
    }
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
      throw new IBHDatabaseException(IBHDatabaseException.DBException.NOTAVAILABLE);
    }

    HashMap<String, String> sett = new HashMap<>();

    if (!isEncrypt()) {
      connString = "jdbc:h2:%s;IFEXISTS=TRUE";
    }
    sett.put("javax.persistence.jdbc.url", String.format(connString, getDbPath(dbName).toString()));
    sett.put("javax.persistence.jdbc.user", dbName);
    if (isEncrypt()) {
      sett.put("javax.persistence.jdbc.password", String.format("%s %s", String.valueOf(encrpwd), String.valueOf(pwd)));
    } else {
      sett.put("javax.persistence.jdbc.password", String.format("%s", String.valueOf(pwd)));
    }
    init(sett);

    // try to get Entity Manager to check if we could really connect to the database
    getEM();

  }

  private static Path getDbPath(String dbName) {

//    String appdatapath = System.getenv("APPDATA");
//    Path dbpath = Paths.get(appdatapath, "IBHSP", DbName);
    Path dbpath = Paths.get(PROPERTIES.getProperty("DBFolder"), dbName);

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
