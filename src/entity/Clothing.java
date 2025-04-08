package entity;

public class Clothing extends Product {
	private String size;
	private String color;
	public Clothing() {
		
	}
	public Clothing(int productId, String size, String color) {
		this.productId = productId;
		this.size = size;
		this.color = color;
	}
	
	public String getSize() {
		return size;
	}
	public void setSize(String size) {
		this.size = size;
	}
	public String getColor() {
		return color;
	}
	public void setColor(String color) {
		this.color = color;
	}
}
