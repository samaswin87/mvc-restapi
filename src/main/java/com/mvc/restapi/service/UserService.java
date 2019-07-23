package com.mvc.restapi.service;

import java.util.List;

import org.springframework.http.ResponseEntity;

import com.mvc.restapi.entity.User;

public interface UserService {

	public List<User> list();
	
	public User find(Integer id);
	
	public ResponseEntity<Object> add(User user);
	
	public ResponseEntity<Object> update(Integer id, User user);
	
	public ResponseEntity<Object> delete(Integer id);
}
