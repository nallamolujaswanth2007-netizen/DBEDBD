package ims.models;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;

@Entity
@Table(name = "inventory_items")
public class InventoryItem {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	Long id;
	String name;
	String category;
	String location;
	int stock;

	@Column(name = "min_stock")
	int minStock;

	@Column(name = "usage_context", columnDefinition = "TEXT")
	String usageContext;

	String unit;

	@Column(name = "daily_usage")
	int dailyUsage;

	@Column(name = "updated_at")
	LocalDateTime updatedAt;

	@PrePersist
	@PreUpdate
	public void markUpdated() {
		this.updatedAt = LocalDateTime.now();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

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

	public LocalDateTime getUpdatedAt() {
		return updatedAt;
	}

	public void setUpdatedAt(LocalDateTime updatedAt) {
		this.updatedAt = updatedAt;
	}
}
