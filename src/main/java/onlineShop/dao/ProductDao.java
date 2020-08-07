package onlineShop.dao;

import onlineShop.model.Product;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;

@Repository
public class ProductDao {

    @Autowired
    private SessionFactory sessionFactory;

    public void addProduct(Product product) {
        Session session = null;
        try {
            session = sessionFactory.openSession();
            session.beginTransaction(); //开启以保证不存在transaction两侧同时成功或者失败
            session.save(product); //保留购买的对象item记录
            session.getTransaction().commit(); //update to database
        } catch (Exception e) {
            e.printStackTrace();
            session.getTransaction().rollback();//返回交易之前的初始状态
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }

    // 这里代码可以优化，与add的内容有相当多的重复部分
    public void deleteProduct(int productId) {
        Session session = null;
        try {
            session = sessionFactory.openSession();
            session.beginTransaction();
            Product product = session.get(Product.class, productId);
            session.delete(product);
            session.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
            session.getTransaction().rollback();//返回交易之前的初始状态
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }

    public void updateProduct(Product product) {
        Session session = null;
        try {
            session = sessionFactory.openSession();
            session.beginTransaction();
            session.saveOrUpdate(product);
            session.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
            session.getTransaction().rollback();//返回交易之前的初始状态
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }

    public Product getProductById(int productId) {
        try (Session session = sessionFactory.openSession()) {
            Product product = session.get(Product.class, productId);
            return product;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<Product> getAllProducts() {
        List<Product> products = new ArrayList<>();
        // try with resource 的写法，这样就会自动地close掉session
        try (Session session = sessionFactory.openSession()) {
            CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
            CriteriaQuery<Product> criteriaQuery = criteriaBuilder.createQuery(Product.class);
            Root<Product> root = criteriaQuery.from(Product.class);
            criteriaQuery.select(root);
            products = session.createQuery(criteriaQuery).getResultList();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return products;
    }
}
