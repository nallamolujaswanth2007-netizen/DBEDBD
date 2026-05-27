package ims.config;

import java.util.Arrays;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import ims.models.ActivityLog;
import ims.models.InventoryItem;
import ims.models.Users;
import ims.repository.ActivityLogRepository;
import ims.repository.InventoryItemRepository;
import ims.repository.UsersRepository;

@Configuration
public class DataSeeder {
	@Bean
	CommandLineRunner seedInventoryData(InventoryItemRepository IIR, ActivityLogRepository ALR, UsersRepository UR) {
		return args -> {
			if (!UR.existsByEmail("admin@inventory.com")) {
				Users admin = new Users();
				admin.setFullname("Inventory Admin");
				admin.setPhone("9999999999");
				admin.setEmail("admin@inventory.com");
				admin.setPassword("admin123");
				admin.setRole(1);
				admin.setStatus(1);
				UR.save(admin);
			}

			if (IIR.count() > 0) {
				return;
			}

			IIR.saveAll(Arrays.asList(
					createItem("Vortex Mixer", "Lab Equipment", "Lab A", 8, 12, "frequently used lab equipment", "units", 5),
					createItem("Laptop Chargers", "Electronics", "Store Room", 24, 18, "low stock electronic accessories", "sets", 3),
					createItem("Sterile Gloves", "Consumables", "Lab B", 16, 20, "low stock medical supplies sterile supplies", "boxes", 7),
					createItem("Barcode Scanners", "Electronics", "Front Desk", 11, 10, "front desk equipment", "units", 2)));

			ALR.saveAll(Arrays.asList(
					createLog("Replenished sterile gloves in Lab B"),
					createLog("Consumed 2 laptop chargers for field ops"),
					createLog("Vortex mixer status updated to low stock"),
					createLog("Barcode scanners reviewed for weekly audit")));
		};
	}

	private InventoryItem createItem(String name, String category, String location, int stock, int minStock,
			String usageContext, String unit, int dailyUsage) {
		InventoryItem item = new InventoryItem();
		item.setName(name);
		item.setCategory(category);
		item.setLocation(location);
		item.setStock(stock);
		item.setMinStock(minStock);
		item.setUsageContext(usageContext);
		item.setUnit(unit);
		item.setDailyUsage(dailyUsage);
		return item;
	}

	private ActivityLog createLog(String label) {
		ActivityLog log = new ActivityLog();
		log.setLabel(label);
		return log;
	}
}
