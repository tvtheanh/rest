package com.example.demo.rest;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dto.UserDTO;
import com.example.demo.repository.UserJpaRepository;


@RestController
@RequestMapping("/api/user")
public class UserRegistrationRestController {
	public static final Logger logger = LoggerFactory.getLogger(UserRegistrationRestController.class);
	
	@Autowired
	private UserJpaRepository userJpaRepository;
	
	@GetMapping("/")
	public ResponseEntity<List<UserDTO>> getAllUsers() {
		List<UserDTO> allUsers = userJpaRepository.findAll();
		return new ResponseEntity<List<UserDTO>>(allUsers, HttpStatus.OK);
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<UserDTO> getUserById(@PathVariable("id") final Long id) {
		UserDTO user = userJpaRepository.findById(id).orElse(null);
		if (user == null) {
			return new ResponseEntity<UserDTO>(HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<UserDTO>(user, HttpStatus.OK);
	}
	
	@PostMapping(value="/", consumes=MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<UserDTO> createUser(@RequestBody final UserDTO user) {
		if (userJpaRepository.findByName(user.getName()) != null) {
			return new ResponseEntity<UserDTO>(HttpStatus.CONFLICT);
		}
		userJpaRepository.save(user);
		return new ResponseEntity<UserDTO>(user, HttpStatus.CREATED);
	}
	
	@PutMapping(value="/{id}", consumes=MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<UserDTO> updateUser(
			@PathVariable("id") final Long id, @RequestBody final UserDTO editedUser) {
		UserDTO user = userJpaRepository.findById(id).orElse(null);
		if (user == null) {
			return new ResponseEntity<UserDTO>(HttpStatus.NOT_FOUND);
		}
		user.setName(editedUser.getName());
		user.setAddress(editedUser.getAddress());
		user.setEmail(editedUser.getEmail());
		userJpaRepository.saveAndFlush(user);
		return new ResponseEntity<UserDTO>(user, HttpStatus.OK);
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<UserDTO> deleteUser(@PathVariable("id") final Long id) {
		UserDTO user = userJpaRepository.findById(id).orElse(null);
		if (user == null) {
			return new ResponseEntity<UserDTO>(HttpStatus.NOT_FOUND);
		}
		userJpaRepository.delete(user);
		return new ResponseEntity<UserDTO>(HttpStatus.NO_CONTENT);
	}
}
