package edu.wpi.cs.wpisuitetng.modules.cal.models.google;

import edu.wpi.cs.wpisuitetng.modules.cal.utils.cache.Pair;

public interface AuthRequest {
	Pair<String, String> getAuthenticationInformation();
}
