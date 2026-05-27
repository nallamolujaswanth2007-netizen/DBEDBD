package ims.dto;

public class InventoryItemUpdateRequest {
	String name;
	String category;
	String location;
	Integer stock;
	Integer minStock;
	String usageContext;
	String unit;
	Integer dailyUsage;

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

	public Integer getStock() {
		return stock;
	}

	public void setStock(Integer stock) {
		this.stock = stock;
	}

	public Integer getMinStock() {
		return minStock;
	}

	public void setMinStock(Integer minStock) {
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

	public Integer getDailyUsage() {
		return dailyUsage;
	}

	public void setDailyUsage(Integer dailyUsage) {
		this.dailyUsage = dailyUsage;
	}
}
