package com.ibh.spdesktop.dal;

import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.Query;

/**
 *
 * @author ihorvath
 */
public class SettingRepository extends BaseRepository<Setting> {

  @Override
  public String getTableName() {
    return "SETTING";
  }

  public Setting getSetting(String name) {
    EntityManager em = DbContext.getEM();
    em.getTransaction().begin();
    List<Setting> ret;

    try {
      Query q = em.createNamedQuery("getByName", Setting.class);
      q.setParameter("name", name);
      ret = q.getResultList();
    } catch (Exception exc) {
      throw exc;
    } finally {
      if (em.isOpen()) {
        em.close();
      }

    }

    return ret.get(0);

  }
}
