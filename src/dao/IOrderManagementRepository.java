package dao;

import java.sql.SQLException;
import java.util.List;

import entity.Clothing;
import entity.Electronics;
import entity.Product;
import entity.User;
import exception.*;

public interface IOrderManagementRepository {
   void createUser(User user) throws UserAlreadyExistsException;
   void createProduct(User user, Product product, Electronics e,String type, Clothing c) throws UserNotFoundException, NotAuthorizedException, SQLException;
   void createOrder(User user, List<Product> products) throws Exception, UserAlreadyExistsException;
   void cancelOrder(int userId, int orderId) throws UserNotFoundException, OrderNotFoundException, Exception;
   List<Product> getAllProducts() throws SQLException;
   List<Product> getOrderByUser(User user) throws UserNotFoundException, SQLException;
}
