package ims.dto;

import java.time.LocalDateTime;

import ims.models.InventoryItem;

public class InventoryItemResponse {
	Long id;
	String name;
	String category;
	String location;
	int stock;
	int minStock;
	String usageContext;
	String unit;
	int dailyUsage;
	LocalDateTime updatedAt;
	String status;

	public InventoryItemResponse(InventoryItem item) {
		this.id = item.getId();
		this.name = item.getName();
		this.category = item.getCategory();
		this.location = item.getLocation();
		this.stock = item.getStock();
		this.minStock = item.getMinStock();
		this.usageContext = item.getUsageContext();
		this.unit = item.getUnit();
		this.dailyUsage = item.getDailyUsage();
		this.updatedAt = item.getUpdatedAt();
		this.status = item.getStock() <= item.getMinStock() ? "Low" : "Healthy";
	}

	public Long getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public String getCategory() {
		return category;
	}

	public String getLocation() {
		return location;
	}

	public int getStock() {
		return stock;
	}

	public int getMinStock() {
		return minStock;
	}

	public String getUsageContext() {
		return usageContext;
	}

	public String getUnit() {
		return unit;
	}

	public int getDailyUsage() {
		return dailyUsage;
	}

	public LocalDateTime getUpdatedAt() {
		return updatedAt;
	}

	public String getStatus() {
		return status;
	}
}
