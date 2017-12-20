package com.ibh.spdesktop.dal;

import com.ibh.spdesktop.bl.Crypt;
import java.io.Serializable;
import java.nio.charset.StandardCharsets;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.PostLoad;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;
import javax.persistence.Transient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author ihorvath
 */
@Entity()
@Table(name = "AUTHENTICATION")
@NamedQueries({
  @NamedQuery(name = "getAuthenticationAll", query = "SELECT OBJECT(au) from Authentication au"),
//  @NamedQuery(
//          name = "getAuthLimited", 
//          query = "SELECT a.title, c.name as category, a.weburl, DATEDIFF('DAY', a.validfrom, CURRENT_DATE()) as numbofdays, 
//  a.description, a.id, c.color FROM AUTHENTICATION a join categories_dict c on a.CATEGORY_ID = c.id")
  @NamedQuery(
          name = "getAuthLimited", 
          query = "SELECT a.title, a.category.name as category, a.weburl, a.validfrom, a.description, a.id, a.category.color FROM Authentication a")
})
public class Authentication implements Serializable {

  private static final Logger LOG = LoggerFactory.getLogger(Authentication.class);
  
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "ID", nullable = false)
  private Integer id;

  @Column(name = "TITLE", nullable = false, unique = true, length = 100)
//  @NotNull
//  @Size(min = 5, max = 100)
  private String title;
  
  @Column(name = "USERNAME", nullable = false, length = 100)
//  @NotNull
//  @Size(min = 4, max = 100)
  private String username;
  
  @Column(name = "PASSWORD", length = 200)
  //@Size(max = 100)
  private String password;
  
  @Column(name = "WEBURL", length = 200)
//  @Size(max = 200)
  private String weburl;
  
  @Column(name = "DESCRIPTION", length = 500)
//  @Size(max = 500)
  private String description;

  @Column(name = "VALIDFROM")
  private LocalDate validfrom;

  @ManyToOne(cascade = CascadeType.PERSIST)
//  @JoinColumn(name = "CATEGORY_ID", nullable = false, referencedColumnName = "ID", foreignKey = @ForeignKey(name = "CATEGORY_ID_FK"))
  @JoinColumn(name = "CATEGORY_ID", nullable = false)
//  @NotNull
  private Category category;

  @OneToMany(targetEntity = AuthProperty.class, cascade = CascadeType.ALL, mappedBy = "authentication")
  private List<AuthProperty> properties;

  @OneToMany(targetEntity = AuthPwdHistory.class, cascade = CascadeType.ALL, mappedBy = "authentication")
  private List<AuthPwdHistory> histories;
  
  @Transient
//  @Size(max = 50)
  private String pwdclear;
  
//  @Transient
//  private boolean ispwdchanged;
//  
//  @Transient
//  private final PropertyChangeSupport changeSupport = new PropertyChangeSupport(this);
//
//  @Transient
//  private final HashMap<String, Set<ConstraintViolation<Authentication>>> errors = new HashMap<>();
//  @Transient
//  private final ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
//  @Transient
//  private final Validator validator = factory.getValidator();
//
//  public void addPropertyChangeListener(PropertyChangeListener listener) {
//    changeSupport.addPropertyChangeListener(listener);
//  }
//
//  public void removePropertyChangeListener(PropertyChangeListener listener) {
//    changeSupport.removePropertyChangeListener(listener);
//  }

  public Authentication() {
  }

//  public HashMap<String, Set<ConstraintViolation<Authentication>>> getErrors() {
//    return errors;
//  }
//
//  public boolean getIsValid() {
//    return errors.isEmpty();
//  }

  @PrePersist
  @PreUpdate
	public void prePersistUpdate() {
		System.out.println("Listening Auth Pre Persist : " + getPassword());
		System.out.println("Listening Auth Pre Persist : " + getPwdclear());
    try {
        // if there is no error on this field, encrypt the pwd
        password = Crypt.encrypt(pwdclear.getBytes(StandardCharsets.UTF_8));
      } catch (InvalidKeyException ex) {
        LOG.error("encrypt error", ex);
      } catch (InvalidAlgorithmParameterException ex) {
        LOG.error("encrypt error", ex);
      } catch (NoSuchAlgorithmException ex) {
        LOG.error("encrypt error", ex);
      }
		System.out.println("Listening Auth Pre Persist : " + getPassword());
		System.out.println("Listening Auth Pre Persist : " + getPwdclear());
	}

  @PostLoad
	public void postLoad() {
		System.out.println("Listening Auth Post Load : " + getPassword());
		System.out.println("Listening Auth Post Load : " + getPwdclear());
      try {
        // if there is no error on this field, encrypt the pwd
        pwdclear = new String(Crypt.decrypt(password));
      } catch (InvalidKeyException ex) {
        LOG.error("encrypt error", ex);
      } catch (InvalidAlgorithmParameterException ex) {
        LOG.error("encrypt error", ex);
      }
		System.out.println("Listening Auth Post Load : " + getPassword());
		System.out.println("Listening Auth Post Load : " + getPwdclear());
	}
  
  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    String old = this.title;
    this.title = title.trim();
//    Set<ConstraintViolation<Authentication>> err = validator.validateProperty(this, "title");
//    if (err.size() > 0) {
//      errors.put("title", err);
//    } else {
//      errors.remove("title");
//    }
//    changeSupport.firePropertyChange("title", old, title);
  }

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    String old = this.username;
    this.username = username.trim();
//    Set<ConstraintViolation<Authentication>> err = validator.validateProperty(this, "username");
//    if (err.size() > 0) {
//      errors.put("username", err);
//    } else {
//      errors.remove("username");
//    }
//    changeSupport.firePropertyChange("username", old, username);
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    String old = this.password;
    this.password = password.trim();
//    Set<ConstraintViolation<Authentication>> err = validator.validateProperty(this, "password");
//    if (err.size() > 0) {
//      errors.put("password", err);
//    } else {
//      errors.remove("password");
//    }
//    changeSupport.firePropertyChange("password", old, password);
  }

  public String getWeburl() {
    return weburl;
  }

  public void setWeburl(String weburl) {
    String old = this.weburl;
    this.weburl = weburl.trim();
//    Set<ConstraintViolation<Authentication>> err = validator.validateProperty(this, "weburl");
//    if (err.size() > 0) {
//      errors.put("weburl", err);
//    } else {
//      errors.remove("weburl");
//    }
//    changeSupport.firePropertyChange("weburl", old, weburl);
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    String old = this.description;
    this.description = description.trim();
//    Set<ConstraintViolation<Authentication>> err = validator.validateProperty(this, "description");
//    if (err.size() > 0) {
//      errors.put("description", err);
//    } else {
//      errors.remove("description");
//    }
//    changeSupport.firePropertyChange("description", old, description);
  }

  public LocalDate getValidfrom() {
    return validfrom;
  }

  public void setValidfrom(LocalDate validfrom) {
    LocalDate old = this.validfrom;
    this.validfrom = validfrom;
//    Set<ConstraintViolation<Authentication>> err = validator.validateProperty(this, "validfrom");
//    if (err.size() > 0) {
//      errors.put("validfrom", err);
//    } else {
//      errors.remove("validfrom");
//    }
//    changeSupport.firePropertyChange("validfrom", old, validfrom);
  }
  
  public Category getCategory() {
    return category;
  }

  public void setCategory(Category category) {
    Category old = this.category;
    this.category = category;
//    Set<ConstraintViolation<Authentication>> err = validator.validateProperty(this, "category");
//    if (err.size() > 0) {
//      errors.put("category", err);
//    } else {
//      errors.remove("category");
//    }
//    changeSupport.firePropertyChange("category", old, category);
    
//    setCategname(category.getName());
  }

  public String getPwdclear() {
    return pwdclear;
  }

  public void setPwdclear(String pwdclear) {
    
    String old = this.pwdclear;
    this.pwdclear = pwdclear.trim();
//    Set<ConstraintViolation<Authentication>> err = validator.validateProperty(this, "pwdclear");
//    if (err.size() > 0) {
//      errors.put("pwdclear", err);
//    } else {
//      errors.remove("pwdclear");
//      try {
//        // if there is no error on this field, encrypt the pwd
//        password = Crypt.encrypt(pwdclear.getBytes(StandardCharsets.UTF_8));
//      } catch (InvalidKeyException ex) {
//        logger.error("encrypt error", ex);
//      } catch (InvalidAlgorithmParameterException ex) {
//        logger.error("encrypt error", ex);
//      } catch (NoSuchAlgorithmException ex) {
//        logger.error("encrypt error", ex);
//      }
//      catch (Exception ex) {
//        logger.error("encrypt error", ex);
//      }
//    }
//    
//    // find out if pwd is changed
//    ispwdchanged = false;
//    if (old == null && this.pwdclear != null && !this.pwdclear.isEmpty()) {
//      ispwdchanged = true;
//    }
//    if (old != null && !old.equals(this.pwdclear)) {
//      ispwdchanged = true;
//    }
//
//    changeSupport.firePropertyChange("pwdclear", old, pwdclear);
  }

//  public void setPwdClearInit() {
//    ispwdchanged = false;
//    try {
//      this.pwdclear = "";
//      if (password != null) {
//        this.pwdclear = new String(Crypt.decrypt(password));
//      }
//    } catch (InvalidKeyException ex) {
//      logger.error("decrypt error", ex);
//    } catch (InvalidAlgorithmParameterException ex) {
//      logger.error("decrypt error", ex);
//    }
//    catch (Exception ex) {
//      logger.error("decrypt error", ex);
//    }
//  }
  
//  public String getCategname() {
////    return categname;
//    if (getCategory() != null) {
//      return getCategory().getName();
//    }
//    else {
//      return "";
//    }
//  }
//
//  public void setCategname(String categname) {
//    logger.debug("setCategname " + categname);
//    
//    String old = this.categname;
//    this.categname = categname.trim();
//    Set<ConstraintViolation<Authentication>> err = validator.validateProperty(this, "categname");
//    logger.debug("err.size " + err.size());
//    if (err.size() > 0) {
//      errors.put("categname", err);
//    } else {
//      errors.remove("categname");
//    }
//    changeSupport.firePropertyChange("categname", old, categname);
//  }


  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  public List<AuthProperty> getProperties() {
    return properties;
  }

  public void setProperties(List<AuthProperty> properties) {
    this.properties = properties;
  }

  public List<AuthPwdHistory> getHistories() {
    return histories;
  }

  public void setHistories(List<AuthPwdHistory> histories) {
    this.histories = histories;
  }

  
//  public boolean getIsPwdChanged() {
//    return ispwdchanged;
//  }  
  
  @Override
  public int hashCode() {
    return Objects.hash(id);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof Authentication)) {
      return false;
    }
    Authentication auth = (Authentication) o;
    return Objects.equals(id, auth.id);
  }

}
