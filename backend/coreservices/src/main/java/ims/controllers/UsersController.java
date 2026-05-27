package ims.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ims.dto.SigninRequest;
import ims.dto.SignupRequest;
import ims.services.AuthService;

@RestController
@RequestMapping("/users")
@CrossOrigin(origins = { "http://localhost:5173", "http://localhost:8000" })
public class UsersController {
	@Autowired
	AuthService AS;

	@PostMapping("/signin")
	public Object signin(@RequestBody SigninRequest data) {
		return AS.signin(data);
	}

	@PostMapping("/signup")
	public Object signup(@RequestBody SignupRequest data) {
		return AS.signup(data);
	}

	@GetMapping("/profile")
	public Object profile(@RequestHeader String Token) {
		return AS.profile(Token);
	}
}
