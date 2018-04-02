/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ibh.spdesktop.dal;

import java.lang.reflect.ParameterizedType;
import java.util.List;
import java.util.Map;
import javax.persistence.EntityManager;
import javax.persistence.Query;

/**
 *
 * @author ihorvath
 */
public abstract class BaseRepository<T> implements IRepository<T> {

	private final T entityType;
	private final String entityTypeName;
	private final String constraintpattern = "(?<constraintname>.*)\\s.* PUBLIC\\.(?<tablename>.*)\\((?<fieldname>.*)\\) VALUES";

	public BaseRepository() {
		this.entityType = (T) (Class) ((ParameterizedType) getClass().getGenericSuperclass())
				.getActualTypeArguments()[0];
		this.entityTypeName = ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0]
				.getTypeName();
	}

	public abstract String getTableName();

	private String getClassName() {

		String className = entityTypeName;
		int firstChar = entityTypeName.lastIndexOf('.') + 1;
		if (firstChar > 0) {
			className = className.substring(firstChar);
		}
		return className;
	}

	// protected IBHDbConstraintException parseConstraintExc(Exception exc) {
	// Pattern regex = Pattern.compile(constraintpattern);
	// String errmess = exc.getConstraintName();
	// Matcher matcher = regex.matcher(errmess);
	// String cname = "";
	// String tname = "";
	// String fname = "";
	//
	// while(matcher.find()) {
	// cname = matcher.group("constraintname");
	// tname = matcher.group("tablename");
	// fname = matcher.group("fieldname");
	// }
	//
	// return new IBHDbConstraintException(cname, tname, fname);
	// }

	@Override
	public T add(T obj) throws IBHDbConstraintException {
		EntityManager em = DbContext.getEM();
		em.getTransaction().begin();
		try {
			em.persist(obj);
			em.getTransaction().commit();
		} catch (Exception exc) {
			throw exc;
			// throw parseConstraintExc(exc);
		} finally {
			if (em.isOpen()) {
				em.close();
			}

		}

		return obj;
	}

	@Override
	public void update(T obj) throws IBHDbConstraintException {
		EntityManager em = DbContext.getEM();
		em.getTransaction().begin();
		try {
			em.merge(obj);
			em.getTransaction().commit();
		} catch (Exception exc) {
			throw exc;
			// throw parseConstraintExc(exc);
		} finally {
			if (em.isOpen()) {
				em.close();
			}

		}
	}

	@Override
	public void delete(int id) {
		EntityManager em = DbContext.getEM();
		em.getTransaction().begin();
		try {
			T inst = em.find((Class<T>) entityType, id);
			em.remove(inst);
			em.getTransaction().commit();
		} catch (Exception exc) {
			throw exc;
			// throw parseConstraintExc(exc);
		} finally {
			if (em.isOpen()) {
				em.close();
			}

		}
	}

	@Override
	public T getById(int id) {
		EntityManager em = DbContext.getEM();
		em.getTransaction().begin();
		T ret;
		try {
			ret = em.find((Class<T>) entityType, id);
			em.getTransaction().commit();
		} catch (Exception exc) {
			throw exc;
			// throw parseConstraintExc(exc);
		} finally {
			if (em.isOpen()) {
				em.close();
			}

		}

		return ret;

	}

	@Override
	public List<?> getList(String queryexpr) {

		EntityManager em = DbContext.getEM();
		em.getTransaction().begin();
		Query q;
		List<?> ret;
		try {
			q = em.createNativeQuery(queryexpr);
			em.getTransaction().commit();
			ret = q.getResultList();
		} catch (Exception exc) {
			throw exc;
			// throw parseConstraintExc(exc);
		} finally {
			if (em.isOpen()) {
				em.close();
			}

		}

		return ret;
	}

	@Override
	public List<?> getList(String queryexpr, Map<String, Object> parameters) {
		throw new UnsupportedOperationException("Not supported yet."); // To change body of generated methods, choose
																		// Tools | Templates.

		// Session sess = DbContext.getSessionFactory().openSession();
		// sess.beginTransaction();
		//
		// Query q = sess.createQuery(queryexpr);
		// for (Map.Entry<String, Object> entry : parameters.entrySet()) {
		// String key = entry.getKey();
		// Object value = entry.getValue();
		// q.setParameter(key, value);
		// }
		//
		// List ret = q.getResultList();
		//
		// sess.getTransaction().commit();
		// sess.close();
		//
		// return ret;
	}

	@Override
	public List<T> getList() {
		EntityManager em = DbContext.getEM();
		em.getTransaction().begin();
		List<?> ret;

		try {
			String qname = String.format("get%sAll", getClassName());
			Query q = em.createNamedQuery(qname, (Class<T>) entityType);
			ret = q.getResultList();
		} catch (Exception exc) {
			throw exc;
		} finally {
			if (em.isOpen()) {
				em.close();
			}

		}

		return (List<T>) ret;

		// try {
		// Statement st = DbContext.getEM().getConnection().createStatement();
		// ResultSet rs = st.executeQuery("select id, name, color from Categories_dict
		// order by name");
		// ArrayList<T> clist = new ArrayList<>();
		// while (rs.next()) {
		// int id = rs.getInt("id");
		// String name = rs.getString("name");
		// String color = rs.getString("color");
		// T inst = (T)Class.forName(this.entityTypeName).newInstance();
		//
		//// T c = new Category();
		//// c.setId(id);
		//// c.setName(name);
		//// c.setColor(color);
		// clist.add(inst);
		// }
		// st.close();
		// return (List<T>)clist;
		// } catch (SQLException exc) {
		// Logger.getLogger(DbContext.class.getName()).log(Level.SEVERE, null, exc);
		// } catch (ClassNotFoundException ex) {
		// Logger.getLogger(BaseRepository.class.getName()).log(Level.SEVERE, null, ex);
		// } catch (InstantiationException ex) {
		// Logger.getLogger(BaseRepository.class.getName()).log(Level.SEVERE, null, ex);
		// } catch (IllegalAccessException ex) {
		// Logger.getLogger(BaseRepository.class.getName()).log(Level.SEVERE, null, ex);
		// }
		// return null;

		// Session sess = DbContext.getSessionFactory().openSession();
		// sess.beginTransaction();
		//
		// String query = "from " + entityTypeName;
		// Query q = sess.createQuery(query);
		// List ret = q.getResultList();
		//
		// sess.getTransaction().commit();
		// sess.close();
		//
		// return ret;
	}

}
