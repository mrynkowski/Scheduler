package scheduler.security;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import scheduler.models.Account;
import scheduler.repositories.AppRepo;

public class SecurityHelper {

	public static Boolean isUserLoggedIn(AppRepo repo, Long accountId){
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if(principal instanceof UserDetails) {
            UserDetails details = (UserDetails)principal;
            Account loggedIn = repo.findByAccountName(details.getUsername());
            if(loggedIn.getId() == accountId) {
            	return true;
            }else{
            	return false;
            }
        }else{
        	return false;
        }	
	}
}
