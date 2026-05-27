package ims.services;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ims.dto.SigninRequest;
import ims.dto.SignupRequest;
import ims.models.Users;
import ims.repository.UsersRepository;

@Service
public class AuthService {
	@Autowired
	UsersRepository UR;

	public Object signup(SignupRequest data) {
		Map<String, Object> response = new HashMap<>();
		try {
			if (UR.existsByEmail(data.getEmail())) {
				throw new Exception("Email ID already registered");
			}

			Users user = new Users();
			user.setFullname(data.getFullname());
			user.setPhone(data.getPhone());
			user.setEmail(data.getEmail());
			user.setPassword(data.getPassword());
			user.setRole(1);
			user.setStatus(1);
			UR.save(user);

			response.put("code", 200);
			response.put("message", "User account has been created");
		} catch (Exception e) {
			response.put("code", 500);
			response.put("message", e.getMessage());
		}
		return response;
	}

	public Object signin(SigninRequest data) {
		Map<String, Object> response = new HashMap<>();
		try {
			Users user = UR.findByEmailAndPassword(data.getUsername(), data.getPassword())
					.orElseThrow(() -> new Exception("Invalid Credentials!"));

			response.put("code", 200);
			response.put("message", "Validation Success");
			response.put("jwt", generateToken(user));
			response.put("fullname", user.getFullname());
			response.put("email", user.getEmail());
			response.put("role", user.getRole());
		} catch (Exception e) {
			response.put("code", 500);
			response.put("message", e.getMessage());
		}
		return response;
	}

	public Object profile(String token) {
		Map<String, Object> response = new HashMap<>();
		try {
			String email = parseEmail(token);
			Users user = UR.findByEmail(email).orElseThrow(() -> new Exception("User not found"));

			response.put("code", 200);
			response.put("fullname", user.getFullname());
			response.put("email", user.getEmail());
			response.put("phone", user.getPhone());
			response.put("role", user.getRole());
			response.put("status", user.getStatus());
		} catch (Exception e) {
			response.put("code", 500);
			response.put("message", e.getMessage());
		}
		return response;
	}

	private String generateToken(Users user) {
		String payload = user.getEmail() + ":" + user.getRole();
		return Base64.getEncoder().encodeToString(payload.getBytes(StandardCharsets.UTF_8));
	}

	private String parseEmail(String token) {
		String payload = new String(Base64.getDecoder().decode(token), StandardCharsets.UTF_8);
		return payload.split(":")[0];
	}
}
