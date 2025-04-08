package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

import entity.Clothing;
import entity.Electronics;
import entity.Product;
import entity.User;
import exception.NotAuthorizedException;
import exception.OrderNotFoundException;
import exception.UserAlreadyExistsException;
import exception.UserNotFoundException;
import util.DBConnUtil;

public class OrderProcessor implements IOrderManagementRepository{
	
	@Override
	public  void createUser(User user) throws UserAlreadyExistsException{
		Connection connection = DBConnUtil.getConnection("db.properties");
		String checkUserQuery = "SELECT * FROM User WHERE userId = ?";
		String insertUserQuery = "INSERT INTO User (userId, userName, password, role) values(?,?,?,?)";
		try (PreparedStatement ps = connection.prepareStatement(checkUserQuery)){
			ps.setInt(1, user.getUserId());
			ResultSet rs = ps.executeQuery();
			if(rs.next()) {
				throw new UserAlreadyExistsException("User already Exists");
			}
			
		}
		catch(SQLException e) {
			e.printStackTrace();
		}
		
		try(PreparedStatement ps = connection.prepareStatement(insertUserQuery)){
			ps.setInt(1,  user.getUserId());
			ps.setString(2,  user.getUserName());
			ps.setString(3, user.getPassword());
			ps.setString(4, user.getRole());
			
			ps.executeUpdate();
		}
		catch(SQLException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void createProduct(User user, Product product, Electronics e,String type, Clothing c) throws SQLException, UserNotFoundException, NotAuthorizedException {
	    Connection connection = DBConnUtil.getConnection("db.properties");

	    String checkUserQuery = "SELECT * FROM User WHERE userId = ? AND role = 'Admin'";
	    String insertProductQuery = "INSERT INTO Product (productId, productName, description, price, quantity, type) VALUES (?, ?, ?, ?, ?, ?)";

	    try (PreparedStatement checkUserStmt = connection.prepareStatement(checkUserQuery)) {
	        checkUserStmt.setInt(1, user.getUserId());
	        ResultSet rs = checkUserStmt.executeQuery();
	        if (!rs.next()) {
	            throw new UserNotFoundException("Admin Not Found");
	        }
	        else {
	        	 try (PreparedStatement ps = connection.prepareStatement(insertProductQuery)) {
	     	        ps.setInt(1, product.getProductId());
	     	        ps.setString(2, product.getProductName());
	     	        ps.setString(3, product.getDescription());
	     	        ps.setDouble(4, product.getPrice());
	     	        ps.setInt(5, product.getQuantity());
	     	        ps.setString(6, type);
	     	        ps.executeUpdate();
	     	    }

	     	   if(type.equals("Electronics")) {
	     	        String insertElectronics = "INSERT INTO Electronics (productId, brand, warrentyPeriod) VALUES (?, ?, ?)";
	     	        try (PreparedStatement ps = connection.prepareStatement(insertElectronics)) {
	     	            ps.setInt(1, product.getProductId());
	     	            ps.setString(2, e.getBrand());
	     	            ps.setInt(3, e.getWarrentyPeriod());
	     	            ps.executeUpdate();
	     	        }
	     	   }
	     	   else {
	     	        String insertClothing = "INSERT INTO Clothing (productId, size, color) VALUES (?, ?, ?)";
	     	        try (PreparedStatement ps = connection.prepareStatement(insertClothing)) {
	     	            ps.setInt(1, product.getProductId());
	     	            ps.setString(2, c.getSize());
	     	            ps.setString(3, c.getColor());
	     	            ps.executeUpdate();
	     	    }
	     	   }
	     	   System.out.println("Product Successfully added !!!");
	        }
	    }
	}

	
	@Override
	public void createOrder(User user, List<Product> products) throws UserAlreadyExistsException, Exception {
		Connection connection = DBConnUtil.getConnection("db.properties");
		String checkUserQuery = "SELECT * FROM User WHERE userId = ?";
		String insertOrderQuery = "INSERT INTO Orders (userId) VALUES (?)";
		String insertOrderProductQuery = "INSERT INTO Order_Products (orderId, productId, quantity) VALUES (?,?,?)";
		String checkProductQuery = "SELECT quantity FROM Product WHERE productId = ?";
		String updateProductStockQuery = "UPDATE Product SET quantity = quantity - ? WHERE productId = ?";
		
			try (PreparedStatement ps = connection.prepareStatement(checkUserQuery)){
				ps.setInt(1, user.getUserId());
				ResultSet rs = ps.executeQuery();
				if(!rs.next()) {
					throw new UserAlreadyExistsException("User not found with ID"+user.getUserId());
				}
				
			}
			
		
			int orderId = 0;
			
			try(PreparedStatement ps = connection.prepareStatement(insertOrderQuery, Statement.RETURN_GENERATED_KEYS)){
				ps.setInt(1, user.getUserId());
				ps.executeUpdate();
				
				try(ResultSet generatedKeys = ps.getGeneratedKeys()){
					if(generatedKeys.next()) {
						orderId = generatedKeys.getInt(1);
					}
					else {
						throw new Exception("Failed to get Order Id");
					}
				}
				
			}
			for(Product product : products) {
				try(PreparedStatement ps = connection.prepareStatement(checkProductQuery)){
					ps.setInt(1,  product.getProductId());
					ResultSet rs = ps.executeQuery();
					
					if(rs.next()) {
						int available = rs.getInt("quantity");
						if(available < product.getQuantity()) {
							throw new Exception("Not enough stock for product");
						}
					}
					else {
						throw new Exception("Product Not Found");
					}
				}
			
			try(PreparedStatement ps = connection.prepareStatement(updateProductStockQuery)){
				ps.setInt(1, product.getQuantity());
				ps.setInt(2,  product.getProductId());
				ps.executeUpdate();
			}
			
			try(PreparedStatement ps = connection.prepareStatement(insertOrderProductQuery)){
				ps.setInt(1, orderId);
				ps.setInt(2, product.getProductId());
				ps.setInt(3, product.getQuantity());
				ps.executeUpdate();
			}
			System.out.println("Order Placed Successfull!!!");
		}
     }
	
	@Override
	public void cancelOrder(int userId, int orderId) throws UserNotFoundException, OrderNotFoundException, Exception {
		Connection connection = DBConnUtil.getConnection("db.properties");
		String checkUserQuery = "SELECT * FROM User WHERE userId = ?";
		String checkOrderQuery = "SELECT * FROM Orders WHERE orderId = ? AND userId = ?";
		String deleteOrderItems = "DELETE FROM Order_Products WHERE orderId = ?";
		String deleteOrders = "DELETE FROM Orders WHERE orderId = ?";
		try (PreparedStatement ps = connection.prepareStatement(checkUserQuery)){
			ps.setInt(1, userId);
			ResultSet rs = ps.executeQuery();
			if(!rs.next()) {
				throw new UserNotFoundException("User Not Found");
			}
			
		}
		catch(SQLException e) {
			e.printStackTrace();
		}
		
		try(PreparedStatement ps = connection.prepareStatement(checkOrderQuery)){
			ps.setInt(1, orderId);
			ps.setInt(2, userId);
			ResultSet rs = ps.executeQuery();
			if(!rs.next()) {
				throw new OrderNotFoundException("Order Not Found");
			}
		}
		
		try(PreparedStatement ps = connection.prepareStatement(deleteOrderItems)){
			ps.setInt(1, orderId);
			ps.executeUpdate();
		}
		
		try(PreparedStatement ps = connection.prepareStatement(deleteOrders)){
			ps.setInt(1, orderId);
			ps.executeUpdate();
		}
		
		System.out.println("Order Cancelled Successfully !");
	}
	
	@Override
	public List<Product> getAllProducts() throws SQLException {
	    Connection connection = DBConnUtil.getConnection("db.properties");
	    List<Product> products = new ArrayList<>();
	    Statement stmt = connection.createStatement();

	    String productsQuery = "SELECT * from Product";
//	    String electronicsQuery = "SELECT e.productId,e.brand, e.warrantyPeriod FROM Product p JOIN Electronics e ON p.productId = e.productId WHERE p.type = 'Electronics'";
//	    String clothingQuery = "SELECT c.productId, c.size, c.color FROM Product p JOIN Clothing c ON p.productId = c.productId WHERE p.type = 'Clothing'";

	    ResultSet rs = stmt.executeQuery(productsQuery);
	    while (rs.next()) {
	        products.add(new Product(
	        		rs.getInt("productId"),
	    	        rs.getString("productName"),
	    	        rs.getString("description"),
	    	        rs.getDouble("price"),
	    	        rs.getInt("quantity"),
	    	        rs.getString("type")
	        ));
	    }
//	    rs = stmt.executeQuery(electronicsQuery);
//	    while (rs.next()) {
//	        products.add(new Electronics(
//	            rs.getInt("productId"),
//	            rs.getString("brand"),
//	            rs.getInt("warrantyPeriod")
//	        ));
//	    }
//
//	    rs = stmt.executeQuery(clothingQuery);
//	    while (rs.next()) {
//	        products.add(new Clothing(
//	            rs.getInt("productId"),
//	            rs.getString("size"),
//	            rs.getString("color")
//	        ));
//	    }

	    return products;
	}
	
	public Electronics getAllElectronics(int productId) throws SQLException {
	    Connection connection = DBConnUtil.getConnection("db.properties");
	    String sql = "SELECT * FROM Electronics WHERE productId = ?";
	    
	    try (PreparedStatement ps = connection.prepareStatement(sql)) {
	        ps.setInt(1, productId);
	        ResultSet rs = ps.executeQuery();

	        if (rs.next()) {
	            return new Electronics(
	                rs.getInt("productId"),
	                rs.getString("brand"),
	                rs.getInt("warrentyPeriod")
	            );
	        } else {
	            return null; 
	        }
	    }
	}

	public Clothing getAllClothing(int productId) throws SQLException {
	    Connection connection = DBConnUtil.getConnection("db.properties");
	    String sql = "SELECT * FROM Clothing WHERE productId = ?";
	    
	    try (PreparedStatement ps = connection.prepareStatement(sql)) {
	        ps.setInt(1, productId);
	        ResultSet rs = ps.executeQuery();

	        if (rs.next()) {
	            return new Clothing(
	                rs.getInt("productId"),
	                rs.getString("size"),
	                rs.getString("color")
	            );
	        } else {
	            return null; 
	        }
	    }
	}

	@Override
	public List<Product> getOrderByUser(User user) throws UserNotFoundException, SQLException {
		Connection connection = DBConnUtil.getConnection("db.properties");
		String checkUserQuery = "SELECT * FROM User WHERE userId = ?";
		try (PreparedStatement ps = connection.prepareStatement(checkUserQuery)){
			ps.setInt(1, user.getUserId());
			ResultSet rs = ps.executeQuery();
			if(!rs.next()) {
				throw new UserNotFoundException("User Not Found");
			}
			
		}
		catch(SQLException e) {
			e.printStackTrace();
		}
		List<Product> products = new ArrayList<>();
		String sql = "SELECT p.* from Product p JOIN Order_Products op ON p.productId = op.productId JOIN Orders o ON o.orderId = op.orderId where o.userId = ?";
		try (PreparedStatement ps = connection.prepareStatement(sql)){
			ps.setInt(1, user.getUserId());
			ResultSet rs = ps.executeQuery();
			while(rs.next()) {
				products.add(new Product( rs.getInt("productId"),
			            rs.getString("productName"),
			            rs.getString("description"),
			            rs.getDouble("price"),
			            rs.getInt("quantity"),
			            rs.getString("type")));
			}
			
		}
		return products;
		
	}

}
