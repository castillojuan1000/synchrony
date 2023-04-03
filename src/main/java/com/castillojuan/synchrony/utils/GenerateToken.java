package com.castillojuan.synchrony.utils;

import java.util.Base64;

public class GenerateToken {
	public static String generateToken(String username) {
	    String token = "Token:" + username;
	    return Base64.getEncoder().encodeToString(token.getBytes());
	}

}
