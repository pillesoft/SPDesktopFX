package com.ibh.spdesktop.bl;

import com.ibh.spdesktop.dal.AuthenticationRepository;
import com.ibh.spdesktop.dal.CategoryRepository;
import com.ibh.spdesktop.dal.DbContext;
import com.ibh.spdesktop.dal.IBHDatabaseException;
import com.ibh.spdesktop.dal.Setting;
import com.ibh.spdesktop.dal.SettingRepository;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import javax.persistence.PersistenceException;
import org.h2.jdbc.JdbcSQLException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author ihorvath
 */
public class BusinessLogic {
  
  private static final Logger LOG = LoggerFactory.getLogger(BusinessLogic.class);
  
  private final CategoryRepository categRepos;
  private final AuthenticationRepository authRepos;
  private final SettingRepository settingRepos;
  private String loggedInName;
  
  public BusinessLogic() {
    categRepos = new CategoryRepository();
    authRepos = new AuthenticationRepository();
    settingRepos = new SettingRepository();
  }

  public CategoryRepository getCategRepos() {
    return categRepos;
  }

  public AuthenticationRepository getAuthRepos() {
    return authRepos;
  }

  public SettingRepository getSettingRepos() {
    return settingRepos;
  }
  
  public String getLoggedInName() {
    return loggedInName;
  }
  
  public boolean login(String dbName, char[] pwd)  throws IBHDatabaseException, JdbcSQLException{
    return login(dbName, pwd, getEncrPwd(pwd));
  }

  public boolean login(String dbName, char[] pwd, char[] encrpwd) throws IBHDatabaseException, JdbcSQLException {
    try {
      DbContext.connect(dbName, pwd, encrpwd);
      loggedInName = dbName;
      
      Setting s = settingRepos.getSetting("DBCREATETIMESTAMP");
      Crypt.setKeyByte(s.getValue());
      return true;
    } catch (IBHDatabaseException | PersistenceException dbe) {
      throw dbe;
    } catch (JdbcSQLException sqlexc) {
      LOG.error("login unexpected exception", sqlexc);
      throw sqlexc;
    } catch (Exception e) {
      LOG.error("login unexpected exception", e);
      throw e;
    }
  }
  public boolean createDB(String dbName, char[] pwd) throws JdbcSQLException {
    try {
      DbContext.createDatabase(dbName, pwd, getEncrPwd(pwd));
      loggedInName = dbName;
      Setting s = settingRepos.add(new Setting("DBCREATETIMESTAMP", LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE)));
      
      Crypt.setKeyByte(s.getValue());
      return true;
    } catch (IBHDatabaseException dbe) {
      throw dbe;
    } catch(JdbcSQLException sqle) {
      throw sqle;
    } catch (Exception e) {
      java.util.logging.Logger.getLogger(BusinessLogic.class.getName()).log(java.util.logging.Level.SEVERE, null, e);
      return false;
    }
  }
  
  private char[] getEncrPwd(char[] pwd) {
    String sepwd = "";
    for (int i = pwd.length; i > 0; i--) {
      sepwd += pwd[i-1];
    }
   
    return sepwd.toCharArray();
  }
//  public Category AddIfNotExistCategory(Category categ) {
//    Category c = getCategRepos().getById(categ.getId());
//    if (c == null) {
//      getCategRepos().Add(categ);
//      return categ;
//    }
//    return c;    
//  }
  
}
