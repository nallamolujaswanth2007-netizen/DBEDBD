package ims.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RestController;

import ims.dto.InventoryAdjustRequest;
import ims.dto.InventoryItemRequest;
import ims.dto.InventoryItemResponse;
import ims.dto.InventoryItemUpdateRequest;
import ims.dto.SummaryResponse;
import ims.models.ActivityLog;
import ims.services.InventoryService;

@RestController
@RequestMapping("/inventory")
@CrossOrigin(origins = { "http://localhost:5173", "http://localhost:8000" })
public class InventoryController {
	@Autowired
	InventoryService IS;

	@GetMapping("/items")
	public List<InventoryItemResponse> getItems(
			@RequestParam(required = false) String search,
			@RequestParam(required = false) String location,
			@RequestParam(name = "semantic_query", required = false) String semanticQuery) {
		return IS.getItems(search, location, semanticQuery);
	}

	@GetMapping("/items/{ID}")
	public InventoryItemResponse getItem(@PathVariable("ID") Long id) {
		return IS.getItem(id);
	}

	@PostMapping("/items")
	@ResponseStatus(HttpStatus.CREATED)
	public InventoryItemResponse createItem(@RequestBody InventoryItemRequest data) {
		return IS.createItem(data);
	}

	@PutMapping("/items/{ID}")
	public InventoryItemResponse updateItem(@PathVariable("ID") Long id, @RequestBody InventoryItemUpdateRequest data) {
		return IS.updateItem(id, data);
	}

	@PatchMapping("/items/{ID}/adjust")
	public InventoryItemResponse adjustItem(@PathVariable("ID") Long id, @RequestBody InventoryAdjustRequest data) {
		return IS.adjustItem(id, data);
	}

	@DeleteMapping("/items/{ID}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void deleteItem(@PathVariable("ID") Long id) {
		IS.deleteItem(id);
	}

	@GetMapping("/locations")
	public List<String> getLocations() {
		return IS.getLocations();
	}

	@GetMapping("/activity")
	public List<ActivityLog> getActivity() {
		return IS.getActivity();
	}

	@GetMapping("/queue")
	public List<InventoryItemResponse> getReplenishmentQueue() {
		return IS.getReplenishmentQueue();
	}

	@GetMapping("/summary")
	public SummaryResponse getSummary() {
		return IS.getSummary();
	}

	@GetMapping("/semantic-queries")
	public List<String> getSemanticQueries() {
		return IS.getSemanticQueries();
	}
}
