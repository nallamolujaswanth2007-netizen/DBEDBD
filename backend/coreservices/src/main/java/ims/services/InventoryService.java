package ims.services;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import ims.dto.InventoryAdjustRequest;
import ims.dto.InventoryItemRequest;
import ims.dto.InventoryItemResponse;
import ims.dto.InventoryItemUpdateRequest;
import ims.dto.SummaryResponse;
import ims.models.ActivityLog;
import ims.models.InventoryItem;
import ims.repository.ActivityLogRepository;
import ims.repository.InventoryItemRepository;

@Service
public class InventoryService {
	@Autowired
	InventoryItemRepository IIR;

	@Autowired
	ActivityLogRepository ALR;

	private final List<String> semanticQueries = Arrays.asList(
			"Low stock electronic items",
			"Frequently used lab equipment",
			"Replenish sterile supplies");

	public List<InventoryItemResponse> getItems(String search, String location, String semanticQuery) {
		return IIR.findAll(Sort.by("id").ascending()).stream()
				.filter(item -> matchesSearch(item, search))
				.filter(item -> location == null || location.isBlank() || location.equals("All")
						|| item.getLocation().equals(location))
				.filter(item -> matchesSemanticQuery(item, semanticQuery))
				.map(InventoryItemResponse::new)
				.collect(Collectors.toList());
	}

	public InventoryItemResponse getItem(Long id) {
		return new InventoryItemResponse(findItem(id));
	}

	public InventoryItemResponse createItem(InventoryItemRequest data) {
		InventoryItem item = new InventoryItem();
		applyCreateFields(item, data);
		InventoryItem saved = IIR.save(item);
		addLog("Added " + saved.getName() + " to " + saved.getLocation());
		return new InventoryItemResponse(saved);
	}

	public InventoryItemResponse updateItem(Long id, InventoryItemUpdateRequest data) {
		InventoryItem item = findItem(id);

		if (data.getName() != null) item.setName(data.getName());
		if (data.getCategory() != null) item.setCategory(data.getCategory());
		if (data.getLocation() != null) item.setLocation(data.getLocation());
		if (data.getStock() != null) item.setStock(Math.max(0, data.getStock()));
		if (data.getMinStock() != null) item.setMinStock(Math.max(0, data.getMinStock()));
		if (data.getUsageContext() != null) item.setUsageContext(data.getUsageContext());
		if (data.getUnit() != null) item.setUnit(data.getUnit());
		if (data.getDailyUsage() != null) item.setDailyUsage(Math.max(0, data.getDailyUsage()));

		InventoryItem saved = IIR.save(item);
		addLog("Updated " + saved.getName() + " inventory details");
		return new InventoryItemResponse(saved);
	}

	public InventoryItemResponse adjustItem(Long id, InventoryAdjustRequest data) {
		InventoryItem item = findItem(id);
		item.setStock(Math.max(0, item.getStock() + data.getAmount()));
		InventoryItem saved = IIR.save(item);

		String action = data.getAmount() > 0 ? "Replenished" : "Consumed";
		String note = data.getNote() == null || data.getNote().isBlank() ? "" : " (" + data.getNote() + ")";
		addLog(action + " " + Math.abs(data.getAmount()) + " " + saved.getUnit() + " of " + saved.getName() + note);

		return new InventoryItemResponse(saved);
	}

	public void deleteItem(Long id) {
		InventoryItem item = findItem(id);
		IIR.delete(item);
		addLog("Removed " + item.getName() + " from inventory monitoring");
	}

	public List<String> getLocations() {
		return IIR.findDistinctLocations();
	}

	public List<ActivityLog> getActivity() {
		return ALR.findTop20ByOrderByCreatedAtDesc();
	}

	public List<InventoryItemResponse> getReplenishmentQueue() {
		return IIR.findReplenishmentQueue().stream()
				.map(InventoryItemResponse::new)
				.collect(Collectors.toList());
	}

	public SummaryResponse getSummary() {
		List<InventoryItem> items = IIR.findAll();
		int totalStock = items.stream().mapToInt(InventoryItem::getStock).sum();
		int lowStockCount = (int) items.stream().filter(item -> item.getStock() <= item.getMinStock()).count();
		int totalLocations = (int) items.stream().map(InventoryItem::getLocation).distinct().count();
		int totalUsage = items.stream().mapToInt(InventoryItem::getDailyUsage).sum();

		return new SummaryResponse(totalStock, lowStockCount, totalLocations, totalUsage, items.size());
	}

	public List<String> getSemanticQueries() {
		return semanticQueries;
	}

	private InventoryItem findItem(Long id) {
		return IIR.findById(id)
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Inventory item not found"));
	}

	private void applyCreateFields(InventoryItem item, InventoryItemRequest data) {
		item.setName(data.getName());
		item.setCategory(data.getCategory());
		item.setLocation(data.getLocation());
		item.setStock(Math.max(0, data.getStock()));
		item.setMinStock(Math.max(0, data.getMinStock()));
		item.setUsageContext(data.getUsageContext());
		item.setUnit(data.getUnit() == null || data.getUnit().isBlank() ? "units" : data.getUnit());
		item.setDailyUsage(Math.max(0, data.getDailyUsage()));
	}

	private boolean matchesSearch(InventoryItem item, String search) {
		if (search == null || search.isBlank()) {
			return true;
		}

		return searchText(item).contains(search.toLowerCase(Locale.ROOT));
	}

	private boolean matchesSemanticQuery(InventoryItem item, String semanticQuery) {
		if (semanticQuery == null || semanticQuery.isBlank()) {
			return true;
		}

		String text = searchText(item);
		for (String term : semanticQuery.toLowerCase(Locale.ROOT).split("\\s+")) {
			if (text.contains(term)) {
				return true;
			}
		}
		return false;
	}

	private String searchText(InventoryItem item) {
		return String.join(" ",
				item.getName(),
				item.getCategory(),
				item.getLocation(),
				item.getUsageContext()).toLowerCase(Locale.ROOT);
	}

	private void addLog(String label) {
		ActivityLog log = new ActivityLog();
		log.setLabel(label);
		ALR.save(log);
	}
}
