package com.castillojuan.synchrony.utils;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

public class DecryptToken {

	public static String decryptToken(String encodedToken) {
	    byte[] decodedBytes = Base64.getDecoder().decode(encodedToken);
	    String decodedToken = new String(decodedBytes, StandardCharsets.UTF_8);
	    
	    
	    if (decodedToken.startsWith("Token:")) {
	        return decodedToken.substring(6); // Remove "Token:" prefix
	    }
	    return null;
	}
}
