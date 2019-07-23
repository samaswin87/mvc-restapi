package com.mvc.restapi;

import static io.restassured.http.ContentType.JSON;
import static io.restassured.module.mockmvc.RestAssuredMockMvc.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;
import static org.springframework.http.HttpStatus.OK;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mvc.restapi.entity.User;
import com.mvc.restapi.service.UserService;

import io.restassured.module.mockmvc.RestAssuredMockMvc;

@RunWith(MockitoJUnitRunner.class)
public class UserControllerTest {
	
	@Mock
	private UserService service;
	
    @InjectMocks
	private UserController controller;
    
    @Before
    public void initialiseRestAssuredMockMvcStandalone() {
        RestAssuredMockMvc.standaloneSetup(controller);
    }
    
    @Test
    public void getAllUsers() {
    	when(service.list()).thenReturn(Collections.emptyList());
    	given()
    	.contentType(JSON)
        .when()
          .get("/users")
        .then()
          .log().ifValidationFails()
          .statusCode(OK.value())
          .contentType(JSON)
          .body(is(equalTo("[]")));
    	
    	List<User> users = new ArrayList<User>();
    	User user = setUser();
    	users.add(user);
    	
    	Gson gsonBuilder = new GsonBuilder().create();
    	String jsonUser = gsonBuilder.toJson(users);
    	when(service.list()).thenReturn(users);
    	given()
    	.contentType(JSON)
        .when()
          .get("/users")
        .then()
          .log().ifValidationFails()
          .statusCode(OK.value())
          .contentType(JSON)
          .body(is(equalTo(jsonUser)));
    }
    
    @Test
    public void getUser() {
    	User user = setUser();
    	String jsonUser = jsonUser(user);

    	when(service.find(1)).thenReturn(user);
    	given()
    	.contentType(JSON)
        .when()
          .get("/user/1")
        .then()
          .log().ifValidationFails()
          .statusCode(OK.value())
          .contentType(JSON)
          .body(is(equalTo(jsonUser)));
    }
    
    @Test
    public void addUser() {
    	User user = setUser();
    	String jsonUser = jsonUser(user);
    	when(service.add(user)).thenReturn(new ResponseEntity<>("Saved the user", HttpStatus.OK));
    	given()
    	.contentType(JSON)
    	.body(jsonUser)
        .when()
          .post("/user")
        .then()
          .log().ifValidationFails()
          .statusCode(OK.value());
    }
    
    @Test
    public void updateUser() {
    	User user = setUser();
    	String jsonUser = jsonUser(user);
    	when(service.update(1, user)).thenReturn(new ResponseEntity<>("User updated", HttpStatus.OK));
    	given()
    	.contentType(JSON)
    	.body(jsonUser)
        .when()
          .put("/user/1")
        .then()
          .log().ifValidationFails()
          .statusCode(OK.value());
    }
    
    @Test
    public void deleteUser() {
    	User user = setUser();
    	String jsonUser = jsonUser(user);
    	when(service.delete(1)).thenReturn(new ResponseEntity<>("User deleted", HttpStatus.OK));
    	given()
    	.contentType(JSON)
    	.body(jsonUser)
        .when()
          .delete("/user/1")
        .then()
          .log().ifValidationFails()
          .statusCode(OK.value());
    }
    
    private User setUser() {
    	User user = new User();
    	user.setId(1);
    	user.setEmail("test@email.com");
    	user.setName("test");
    	user.setPassword("password");
    	return user;
    }
    
    private String jsonUser(User user) {
    	Gson gsonBuilder = new GsonBuilder().create();
    	return gsonBuilder.toJson(user);
    }
	
}
