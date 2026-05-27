package ims.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import ims.models.InventoryItem;

@Repository
public interface InventoryItemRepository extends JpaRepository<InventoryItem, Long> {
	@Query("select distinct I.location from InventoryItem I order by I.location")
	public List<String> findDistinctLocations();

	@Query("select I from InventoryItem I where I.stock <= I.minStock order by (I.minStock - I.stock) desc")
	public List<InventoryItem> findReplenishmentQueue();
}
