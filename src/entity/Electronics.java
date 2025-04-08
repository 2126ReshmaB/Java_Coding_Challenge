package entity;

public class Electronics extends Product {
	private String brand;
	private int warrentyPeriod;
	
	public Electronics() {
		
	}
	public Electronics(int productId, String brand, int warrentyPeriod) {
		this.productId = productId;
		this.brand = brand;
		this.warrentyPeriod = warrentyPeriod;
	}
	
	public String getBrand() {
		return brand;
	}
	public void setBrand(String brand) {
		this.brand = brand;
	}
	public int getWarrentyPeriod() {
		return warrentyPeriod;
	}
	public void setWarrentyPeriod(int warrentyPeriod) {
		this.warrentyPeriod = warrentyPeriod;
	}
	
}
