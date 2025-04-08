create schema OrderManagementSystem;
use ordermanagementsystem;

create table Product(
   productId int,
   productName varchar(20),
   description varchar(20),
   price double,
   quantityInStock int,
   type varchar(20)
);

create table Clothing(
    productId int,
    size varchar(20),
    color varchar(20),
    foreign key (productId) references Product (productId)
);

create table Electronics(
    productId int,
    brand varchar(20),
    warrentyPeriod int,
    foreign key (productId) references Product (productId)
);

create table User(
    userId int,
    userName varchar(20),
    password varchar(20),
    role varchar(20)
);

create table Order_Products(
     orderId int,
     productId int,
     quantity int
     );
     
CREATE TABLE Orders (
    orderId INT PRIMARY KEY AUTO_INCREMENT,
    userId INT,
    FOREIGN KEY (userId) REFERENCES User(userId)
);
