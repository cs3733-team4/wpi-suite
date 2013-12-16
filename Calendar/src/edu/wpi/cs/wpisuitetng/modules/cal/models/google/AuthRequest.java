package edu.wpi.cs.wpisuitetng.modules.cal.models.google;

import java.net.MalformedURLException;

import com.google.gdata.util.AuthenticationException;

import edu.wpi.cs.wpisuitetng.modules.cal.utils.cache.Pair;

public interface AuthRequest {
	Pair<String, String> getAuthenticationInformation();
	
	void handleError(AuthenticationException ae);
	
	void handleError(MalformedURLException ae);
	
	void succede();
}
