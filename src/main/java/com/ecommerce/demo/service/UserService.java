package com.ecommerce.demo.service;

import com.ecommerce.demo.dto.ItemDTO;
import com.ecommerce.demo.dto.ProductDTO;
import com.ecommerce.demo.dto.UserDTO;
import com.ecommerce.demo.dto.UserOrderResponse;
import com.ecommerce.demo.exception.OrderNotFoundException;
import com.ecommerce.demo.exception.UserNameNotFoundException;
import com.ecommerce.demo.model.Product;
import com.ecommerce.demo.model.User;
import com.ecommerce.demo.repository.UserRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder encoder = new BCryptPasswordEncoder();

    private final Logger logger = LoggerFactory.getLogger(UserService.class);
    
    public String registerNewUserAccount(User user) {
    	if(userRepository.existsByEmail(user.getEmail())){    		
            return "Mail ID Already Exists";
        } else {
            user.setPassword(encoder.encode(user.getPassword()));
            userRepository.save(user);
            return "Done";
        }
    }

    public String login(User user){
    	Optional<User> existUser = userRepository.findByEmail(user.getEmail());
    	
    	if(existUser.isPresent()) {
        	String hashedPassword = existUser.get().getPassword();
            
            String password = user.getPassword();
                    

            boolean isPasswordMatch = encoder.matches(password, hashedPassword);

        	if((existUser.get().getEmail()).equals(user.getEmail())) {
        		//encode the password
        		if(isPasswordMatch) {
        			return user.getEmail() + " logged in successfully";
        		} else {
        			return user.getEmail() + " password incorrect";
        		}
        	}
    	} 
		return user.getEmail() + " invalid.";    	
    }
   
    
    public String delete(User user) {
    	Optional<User> existUser = userRepository.findByEmail(user.getEmail());
    	
    	if(existUser.isPresent()) {
        	String hashedPassword = existUser.get().getPassword();
            
            String password = user.getPassword();
                    

            boolean isPasswordMatch = encoder.matches(password, hashedPassword);

        	if((existUser.get().getEmail()).equals(user.getEmail())) {
        		//encode the password
        		if(isPasswordMatch) {
        			userRepository.deleteById(existUser.get().getId());
        			return user.getEmail() + " deleted in successfully";
        		} else {
        			return user.getEmail() + " password incorrect";
        		}
        	}
    	} 
		return user.getEmail() + " not found.";
    }
    
    public Optional<UserDTO> findUserByEmail(String email) {
        Optional<User> userDetails = userRepository.findByEmail(email);
        if(userDetails.isEmpty()) {
    		throw new UsernameNotFoundException("Email ID : " + email + " is not found");
        } else {
        	return userDetails.map(
        			order -> {
    					User user = userDetails.get();
    					UserDTO userDTO = new UserDTO(user.getId(), user.getUsername(), user.getEmail());

    			            // Create and return the UserDTO
    			            return userDTO;
			        });
        }
        
    }
}
