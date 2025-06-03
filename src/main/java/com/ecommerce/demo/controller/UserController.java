package com.ecommerce.demo.controller;

import com.ecommerce.demo.dto.UserDTO;
import com.ecommerce.demo.dto.UserOrderResponse;
import com.ecommerce.demo.exception.OrderNotFoundException;
import com.ecommerce.demo.model.Order;
import com.ecommerce.demo.model.User;
import com.ecommerce.demo.service.UserService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;
    private final Logger logger = LoggerFactory.getLogger(UserController.class);

    @PostMapping("/register")
    public String registerUser(@RequestBody User user) {
        return userService.registerNewUserAccount(user);
    }

    @GetMapping("/{email}")
    public ResponseEntity<Optional<UserDTO>> getUserByEmail(@PathVariable String email) {

    	Optional<UserDTO> user = userService.findUserByEmail(email);
    	
    	if(user.isPresent()) {
    		return new ResponseEntity<Optional<UserDTO>>(user, HttpStatus.OK);
    	} else {
    		throw new OrderNotFoundException("Order with ID : " + email + " not found");
    	}
    }
    
    @PostMapping("/login")
    public String loginToApplication(@RequestBody User user){
    	return userService.login(user);
	}
    
    @DeleteMapping("/delete")
    public String deleteUserAccount(@RequestBody User user) {
    	return userService.delete(user);
    }
    
    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<String> handleOrderNotFoundException(UsernameNotFoundException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
    }
    
    
}
