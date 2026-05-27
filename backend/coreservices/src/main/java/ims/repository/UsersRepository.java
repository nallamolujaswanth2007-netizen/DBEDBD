package ims.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import ims.models.Users;

@Repository
public interface UsersRepository extends JpaRepository<Users, Long> {
	public Optional<Users> findByEmail(String email);

	public Optional<Users> findByEmailAndPassword(String email, String password);

	public boolean existsByEmail(String email);
}
