
package com.ibh.spdesktop.dal;

import java.util.List;
import java.util.Map;

/**
 *
 * @author ihorvath
 */
public interface IRepository<T> {
  
  T add(T obj);
  void update(T obj);
  void delete(int id);
  T getById(int id);

  List<?> getList(String queryexpr, Map<String, Object> parameters);
  List<?> getList(String queryexpr);
  List<T> getList();
}
