package com.example.restfulwebservice.user;

import java.net.URI;
import java.util.List;

import javax.validation.Valid;

import org.hibernate.EntityMode;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@RestController
public class UserController {

	private UserDaoService service;
	
	public UserController(UserDaoService service) {
		
		this.service = service;
	}
	
	@GetMapping("/users")
	public List<User> retrieveAllUser() {
		
		return service.fineAll();
	}
	
	// GET /users/1 or /users/10 -> String
	@GetMapping("/users/{id}")
	public EntityModel<User> retrieveUser(@PathVariable int id) {
		
		User foundUser = service.fineOne(id);
		
		if (foundUser == null) {
			throw new UserNotFoundException(String.format("ID[%s] is not found", id));
		}

		// hateoas
		// spring boot ~ 2.1
//		Resource<User> resource = new Resource<>(foundUser);
//		ControllerLinkBuilder linkTo = lintTo(methodOn(this.getClass()).retreveAllUsers());
//		resource.add(linkTo.withRel("all-users"));

		// spring boot 2.2 ~
		EntityModel<User> model = EntityModel.of(foundUser);
		WebMvcLinkBuilder linkTo = WebMvcLinkBuilder.linkTo(
				WebMvcLinkBuilder.methodOn(this.getClass()).retrieveAllUser());
		model.add(linkTo.withRel("all-users"));

		return model;
	}
	
	@PostMapping("/users")
	public ResponseEntity<User> createUser(@Valid @RequestBody User user) {
		
		User savedUser = service.save(user);
		
		URI location = ServletUriComponentsBuilder.fromCurrentRequest()
			.path("/{id}")
			.buildAndExpand(savedUser.getId())
			.toUri();
		
		return ResponseEntity.created(location).build();
	}
	
	@DeleteMapping("/users/{id}")
	public void deleteUser(@PathVariable int id) {
		
		User user = service.deleteById(id);
		
		if (user == null) {
			
			throw new UserNotFoundException(String.format("ID[%s] is not found", id));
		}
	}
}
