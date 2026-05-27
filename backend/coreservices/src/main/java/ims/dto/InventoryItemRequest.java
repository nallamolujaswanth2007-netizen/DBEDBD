package ims.dto;

public class InventoryItemRequest {
	String name;
	String category;
	String location;
	int stock;
	int minStock;
	String usageContext;
	String unit;
	int dailyUsage;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public int getStock() {
		return stock;
	}

	public void setStock(int stock) {
		this.stock = stock;
	}

	public int getMinStock() {
		return minStock;
	}

	public void setMinStock(int minStock) {
		this.minStock = minStock;
	}

	public String getUsageContext() {
		return usageContext;
	}

	public void setUsageContext(String usageContext) {
		this.usageContext = usageContext;
	}

	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}

	public int getDailyUsage() {
		return dailyUsage;
	}

	public void setDailyUsage(int dailyUsage) {
		this.dailyUsage = dailyUsage;
	}
}
