package ims.dto;

public class SummaryResponse {
	int totalStock;
	int lowStockCount;
	int totalLocations;
	int totalUsage;
	int itemCount;

	public SummaryResponse(int totalStock, int lowStockCount, int totalLocations, int totalUsage, int itemCount) {
		this.totalStock = totalStock;
		this.lowStockCount = lowStockCount;
		this.totalLocations = totalLocations;
		this.totalUsage = totalUsage;
		this.itemCount = itemCount;
	}

	public int getTotalStock() {
		return totalStock;
	}

	public int getLowStockCount() {
		return lowStockCount;
	}

	public int getTotalLocations() {
		return totalLocations;
	}

	public int getTotalUsage() {
		return totalUsage;
	}

	public int getItemCount() {
		return itemCount;
	}
}
