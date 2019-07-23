package com.mvc.restapi;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.mvc.restapi.entity.User;
import com.mvc.restapi.service.UserService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@Api(value = "UserController")
@RestController
public class UserController {

	@Autowired
	private UserService service;

	@ApiOperation(value = "Get list of Users", response = User.class, tags = "Users", produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Success|OK") })
	@GetMapping(value = "/users", produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ResponseEntity<Object> allUsers() {
		return new ResponseEntity<>(service.list(), HttpStatus.OK);
	}

	@ApiOperation(value = "Get User", response = User.class, tags = "Users", produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Success|OK") })
	@GetMapping(value = "/user/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ResponseEntity<Object> getUser(@PathVariable("id") int id) {
		return new ResponseEntity<>(service.find(id), HttpStatus.OK);
	}

	@ApiOperation(value = "Add User", response = ResponseEntity.class, tags = "Users", produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Success|OK") })
	@PostMapping(value = "/user", produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ResponseEntity<Object> addUser(@RequestBody User user) {
		ResponseEntity<Object> status = service.add(user);
		return status;
	}

	@ApiOperation(value = "Update User", response = ResponseEntity.class, tags = "Users", produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Success|OK") })
	@PutMapping(value = "/user/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ResponseEntity<Object> updateUser(@PathVariable("id") int id, @RequestBody User user) {
		ResponseEntity<Object> status = service.update(id, user);
		return status;
	}

	@ApiOperation(value = "Delete User", response = ResponseEntity.class, tags = "Users", produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Success|OK") })
	@RequestMapping(value = "/user/{id}", produces = MediaType.APPLICATION_JSON_VALUE, method = RequestMethod.DELETE)
	public @ResponseBody ResponseEntity<Object> deleteUser(@PathVariable("id") int id) {
		ResponseEntity<Object> status = service.delete(id);
		return status;
	}

	@ResponseBody
	@ExceptionHandler(HttpMediaTypeNotAcceptableException.class)
	public String handleHttpMediaTypeNotAcceptableException() {
		return "acceptable MIME type:" + MediaType.APPLICATION_JSON_VALUE;
	}
}
