package onlineShop.dao;

import onlineShop.model.Cart;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class CartDao {

    @Autowired
    private SessionFactory sessionFactory;

    public Cart getCartById(int cartId){
        Cart cart = null;
        try (Session session = sessionFactory.openSession()){
            cart = (Cart) session.get(Cart.class, cartId);
            session.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return cart;
    }
}


