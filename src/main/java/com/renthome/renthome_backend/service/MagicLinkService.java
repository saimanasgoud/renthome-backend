package com.renthome.renthome_backend.service;

import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
public class MagicLinkService {

    private Map<String, TokenData> tokenStore = new HashMap<>();

    static class TokenData {
        String email;
        long expiry;

        TokenData(String email, long expiry) {
            this.email = email;
            this.expiry = expiry;
        }
    }

    public String generateToken(String email) {
        String token = UUID.randomUUID().toString();

        long expiry = System.currentTimeMillis() + (5 * 60 * 1000); // 5 min

        tokenStore.put(token, new TokenData(email, expiry));

        return token;
    }

    public String validateToken(String token) {
        TokenData data = tokenStore.get(token);

        if (data == null) return null;

        if (System.currentTimeMillis() > data.expiry) {
            // tokenStore.remove(token);
            return null;
        }

        // tokenStore.remove(token); // one-time use
        return data.email;
    }
}