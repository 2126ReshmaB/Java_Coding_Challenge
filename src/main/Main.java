package main;

import java.util.*;

import entity.Clothing;
import entity.Electronics;
import entity.Product;
import entity.User;
import dao.OrderProcessor;


public class Main {
     public static void main(String args[]) throws Exception {
    	 Scanner sc = new Scanner(System.in);
    	 OrderProcessor op = new OrderProcessor();
    	 
    	 while(true) {
    		 System.out.println("------------- Welcome to Order Management System -------------");
    		 System.out.println("==============================================================");
    		 System.out.println(" 1. Create User \n 2. Create Product \n 3. Create Order \n 4. Calcel Order \n 5. Get All Products \n 6. Get Orders By User \n 7. Exit");
    		 System.out.println("==================");
    		 System.out.print("Enter you choice : ");
    		 int ch = sc.nextInt();
    		 
    		 switch(ch) {
    		 case 1:
    			 System.out.print("UserId : ");
    			 int userId = sc.nextInt();
    			 System.out.println();
    			 System.out.print("UserName : ");
    			 String userName = sc.next();
    			 System.out.println();
    			 System.out.print("Password : ");
    			 String password = sc.next();
    			 System.out.println();
    			 System.out.print("Role : ");
    			 String role = sc.next();
    			 
    			 User user = new User(userId, userName, password, role);
    			 op.createUser(user);
    			 
    			 System.out.println("User Created Successful !!!");
    			 break;
    		 case 2:
    			 System.out.print("Enter UserID (If you are an Admin) : ");
    			 User admin = new User();
    			 admin.setUserId(sc.nextInt());
    			 System.out.print("Enter type of Product : ");
    			 String type = sc.next();
    			    System.out.println("Enter Product ID: ");
				    int id = sc.nextInt();
				    System.out.println("Enter Product Name: ");
				    String name = sc.next();
				    System.out.println("Enter Description: ");
				    String desc = sc.next();
				    System.out.println("Enter Price: ");
				    double price = sc.nextDouble();
				    System.out.println("Enter Quantity: ");
				    int quantity = sc.nextInt();
    			 
    			 if (type.equalsIgnoreCase("Electronics")) {
    				    Product p = new Product(id, name, desc, price, quantity, "Electronics");
    				    System.out.println("Enter Brand: ");
    				    String brand = sc.next();
    				    System.out.println("Enter Warranty Period (months): ");
    				    int warranty = sc.nextInt();

    				    Electronics e = new Electronics(id, brand, warranty);
    				    op.createProduct(admin, p,e, "Electronics", null);
    			 }
    			 else {
    				 Product p = new Product(id, name, desc, price, quantity, "Clothing");
    				 System.out.println("Enter Size : ");
    				 String size = sc.next();
    				 System.out.println("Enter Color : ");
    				 String color = sc.next();
    				 
    				 Clothing c = new Clothing(id, size, color);
    				 op.createProduct(admin, p, null, "Clothing", c);
    			 }
    			 break;
    		 case 3:
    			 System.out.println("Enter UserId : ");
    			 User user1 = new User();
    			 user1.setUserId(sc.nextInt());
    			 
    			 Product p1 = new Product();
    			 System.out.println("Enter ProductID : ");
    			 p1.setProductId(sc.nextInt());
    			 System.out.println("Enter Quantity : ");
    			 p1.setQuantity(sc.nextInt());
    			 List<Product> al = new ArrayList<>();
    			 al.add(p1);
    			 op.createOrder(user1, al);
    			 break;
    		 case 4:
    			 System.out.println("For Cancelling the Order Enter you ID and OrderID.");
    			 System.out.println("Enter UserId : ");
    			 int userID = sc.nextInt();
    			 System.out.println("Enter OrderId : ");
    			 int orderID = sc.nextInt();
    			 op.cancelOrder(userID, orderID);
    			 break;
    		 case 5:
    			  List<Product> allProducts = op.getAllProducts();
    			  for(Product p : allProducts) {
    				  String typeOfProduct = p.getType();
    				  System.out.print("ProductId="+p.getProductId()+" ProductName="+p.getProductName()+" Description="+p.getDescription()+" price="+p.getPrice()+" Quantity="+p.getQuantity()+" Type="+p.getType());
    				  if(typeOfProduct.equals("Electronics")) {
    					  Electronics allElectronics = op.getAllElectronics(p.getProductId());
    					  System.out.print(" Brand="+allElectronics.getBrand());
    					  System.out.print(" WarrantyPeriod="+allElectronics.getWarrentyPeriod());
    					  System.out.println();
    				  }
    				  else if(typeOfProduct.equals("Clothing")){
    					  Clothing allClothingProducts = op.getAllClothing(p.getProductId());
    					  System.out.print(" Size="+allClothingProducts.getSize());
    					  System.out.print(" Color="+allClothingProducts.getColor());
    					  System.out.println();
    				  }
    			  }
    			  break;
    		 case 6:
    			 System.out.println("Enter you ID : ");
    			 User user2 = new User();
    			 user2.setUserId(sc.nextInt());
    			 List<Product> all = op.getOrderByUser(user2);
    			 for(Product p : all) {
   				  System.out.println("ProductId="+p.getProductId()+" ProductName="+p.getProductName()+" Description="+p.getDescription()+" price="+p.getPrice()+" Quantity="+p.getQuantity()+" Type="+p.getType());
    			 }
    			 if(all.size() == 0) {
    				 System.out.println("Ooops... you haven't Ordered anything. Go and Order now.");
    			 }
    			 break;
    		default:
    			break;
    		 }
    		 
    	 }
     }
}
