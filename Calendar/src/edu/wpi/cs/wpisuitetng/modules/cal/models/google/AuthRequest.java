package edu.wpi.cs.wpisuitetng.modules.cal.models.google;

import java.net.MalformedURLException;

import com.google.gdata.util.AuthenticationException;

import edu.wpi.cs.wpisuitetng.modules.cal.utils.cache.Pair;

public interface AuthRequest {
	
	/**
	 * get the authentication information that is provided by the user
	 * 
	 * @return a pair of username and password values
	 */
	Pair<String, String> getAuthenticationInformation();
	
	/**
	 * the username and password dont correspond to a valid google account
	 * 
	 * @param ae an authException
	 */
	void handleError(AuthenticationException ae);
	
	/**
	 * The user exists, but they have their calendar disabled
	 * 
	 * @param ae a URL error
	 */
	void handleError(MalformedURLException ae);
	
	/**
	 * the login was successful! handle this however you wish
	 */
	void succede();
}
