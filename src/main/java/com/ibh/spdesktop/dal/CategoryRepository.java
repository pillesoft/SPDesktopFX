package com.ibh.spdesktop.dal;

/**
 *
 * @author ihorvath
 */
public class CategoryRepository extends BaseRepository<Category> {
  
  @Override
  public String getTableName() {
    return "CUSTOMER_DICT";
  }
  /*
  public Category AddorGet(String categname) {
    Category c;
    HashMap<String, Object> param = new HashMap<>();
    param.put("name", categname);
    List l = getList("from Category c where c.name = :name", param);
    if (l.isEmpty()) {
      c = add(new Category(categname));
    } else {
      c = (Category) l.get(0);
    }
    return c;
  }

//  public List<String> getCategoryName() {
//    List<String> l = getList("select c.name from Category order by c.name");
//    return l;
//  }
  @Override
  public Category add(Category obj) {
    throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
  }

  @Override
  public void update(Category obj) {
    throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
  }

  @Override
  public void delete(Category obj) {
    throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
  }

  @Override
  public Category getById(int id) {
    throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
  }

  @Override
  public List getList(String queryexpr, Map<String, Object> parameters) {
    throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
  }

  @Override
  public List getList(String queryexpr) {
    throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
  }

  @Override
  public List<Category> getList() {
    try {
      Statement st = DbContext.getConnection().createStatement();
      ResultSet rs = st.executeQuery("select id, name, color from Categories_dict order by name");
      ArrayList<Category> clist = new ArrayList<>();
      while (rs.next()) {
        int id = rs.getInt("id");
        String name = rs.getString("name");
        String color = rs.getString("color");
        Category c = new Category();
        c.setId(id);
        c.setName(name);
        c.setColor(color);
        clist.add(c);
      }
      st.close();
      return clist;
    } catch (SQLException exc) {
      Logger.getLogger(DbContext.class.getName()).log(Level.SEVERE, null, exc);
      return null;

    }
  }
*/

}
