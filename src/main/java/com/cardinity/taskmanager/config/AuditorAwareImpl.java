package com.cardinity.taskmanager.config;

import java.util.Optional;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;


public class AuditorAwareImpl implements AuditorAware<String> {
    
	@Override
    public Optional<String> getCurrentAuditor() {
       
        if(SecurityContextHolder.getContext().getAuthentication() !=null ){
			if(!(SecurityContextHolder.getContext().getAuthentication().getPrincipal() instanceof String)) {
		        User principal = (User)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		        return Optional.of(principal.getUsername());
			}
        }
        
      return Optional.of("anonymous");
    }
        
}
