package com.cardinity.taskmanager.config;

import java.util.Objects;

import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.cardinity.taskmanager.auth.Role;
import com.cardinity.taskmanager.auth.User;
import com.cardinity.taskmanager.auth.UserRepository;

@Service
@Transactional
public class InitialLoadData implements ApplicationListener<ContextRefreshedEvent>{
private static final Logger LOGGER = LoggerFactory.getLogger(InitialLoadData.class);
	
    boolean alreadySetup = false;
 
    @Autowired
	private UserRepository userRepository;
    
    @Autowired
    private PasswordEncoder passwordEncoder;
    
    @Value("${init.admin.username}")
    private String adminUsername;
    
    @Value("${init.admin.password}")
    private String adminPassword;
    
    @Value("${init.user.username}")
    private String username;
    
    @Value("${init.user.password}")
    private String password;
  
    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
  
        if (alreadySetup)
            return;
                
        try {
        
	        User adminUser = userRepository.findByUsername(adminUsername);
	        if(Objects.isNull(adminUser)){
		        adminUser = new User(adminUsername,passwordEncoder.encode(adminPassword));		     
		        adminUser.setRole(Role.ROLE_ADMIN);		        
		        adminUser.setEnabled(true);		        
		        userRepository.save(adminUser);	
	        }
	        
	        User user = userRepository.findByUsername(username);
	        if(Objects.isNull(user)){
		        user = new User(username,passwordEncoder.encode(password));		     
	    		user.setRole(Role.ROLE_USER);		        
	    		user.setEnabled(true);		        
		        userRepository.save(user);	        
	        }	        
        
        } catch (Exception ex) {
	    	
	    	 LOGGER.error(ex.getMessage());
	    	 	    	 
	    }        
       
        alreadySetup = true;
    }
    
}
