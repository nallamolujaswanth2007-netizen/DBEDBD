package ims.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import ims.models.ActivityLog;

@Repository
public interface ActivityLogRepository extends JpaRepository<ActivityLog, Long> {
	public List<ActivityLog> findTop20ByOrderByCreatedAtDesc();
}
