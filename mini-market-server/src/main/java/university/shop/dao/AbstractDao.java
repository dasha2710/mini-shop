package university.shop.dao;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;


/**
 * Created by Dasha on 18.11.2015.
 */
@Repository
public abstract class AbstractDao<T> {
    @Autowired
    protected SessionFactory sessionFactory;

    public void save(T object) {
        sessionFactory.getCurrentSession().saveOrUpdate(object);
    }

    public void delete(T object) {
        sessionFactory.getCurrentSession().delete(object);
    }

}
