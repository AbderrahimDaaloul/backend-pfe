package com.daaloul.BackEnd.services;



import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.*;
import java.util.function.Function;

@Service
public class JwtService {
    // Replace this with a fixed secret key
    private static final String SECRET_KEY = "Daaloulyadaaloulrakmchityadaaloula7a7a7a7a7a7a7a7a7amchiiiiinaaaaa";

    private SecretKey getKey() {
        byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(keyBytes);
    }
    public JwtService(){
//        try {
//            KeyGenerator keyGenerator= KeyGenerator.getInstance("HmacSHA256");
//            SecretKey sk = keyGenerator.generateKey();
//            secretKey = Base64.getEncoder().encodeToString(sk.getEncoded());
//
//        }catch (NoSuchAlgorithmException e){
//            throw new RuntimeException(e);
//        }
    }



    public String generateToken(String email, UUID id, String role) {
        return Jwts.builder()
                .setSubject(email)                     // Email as the subject
                .claim("id", id)                       // Add user ID as a custom claim
                .claim("role", role)                   // Add user role as a custom claim
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 30 * 60 * 60 * 1000)) // 30 hours expiry
                .signWith(getKey(), SignatureAlgorithm.HS256)
                .compact();
    }




    public String extractUserName(String token) {
        // extract the username from jwt token
        return extractClaim(token, Claims::getSubject);
    }
    public UUID extractId(String token) {
        return extractClaim(token, c -> (UUID) c.get("id"));
    }

    public String extractRole(String token) {
        return extractClaim(token, c -> (String) c.get("role"));
    }

//    private <T> T extractClaim(String token, Function<Claims, T> claimResolver) {
//        final Claims claims = extractAllClaims(token);
//        return claimResolver.apply(claims);
//    }
    private <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parser()
                .verifyWith(getKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public boolean validateToken(String token, UserDetails userDetails) {
        final String userName = extractUserName(token);
        System.out.println("username from token: "+userName);
        System.out.println("username from userdetails: "+userDetails.getUsername());
        System.out.println(userName.equals(userDetails.getUsername()));
        System.out.println("isTokenExpired: "+isTokenExpired(token));
        return (userName.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }
}


//package com.daaloul.BackEnd.services;
//
//import io.jsonwebtoken.Claims;
//import io.jsonwebtoken.Jwts;
//import io.jsonwebtoken.security.Keys;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.stereotype.Service;
//
//import javax.crypto.SecretKey;
//import java.util.Base64;
//import java.util.Date;
//import java.util.function.Function;
//
//@Service
//public class JwtService {
//
//    // ✅ Use a strong, 32-byte secret key (Base64 encoded)
//    private static final String SECRET_KEY = "thisIsA32ByteLongSecretKeyForJWTAA4"; // Directly use the raw secret key
//
//    // ✅ Generate JWT Token
//    public String generateToken(String email) {
//        return Jwts.builder()
//                .setSubject(email) // Set the subject (e.g., email)
//                .setIssuedAt(new Date(System.currentTimeMillis())) // Set issued time
//                .setExpiration(new Date(System.currentTimeMillis() + 30 * 60 * 60 * 1000)) // 30 hours expiry
//                .signWith(getKey(), io.jsonwebtoken.SignatureAlgorithm.HS256) // Sign with HS256
//                .compact(); // Compact into a JWT string
//    }
//
//    // ✅ Get the SecretKey for signing and verification
//    private SecretKey getKey() {
//        // Convert the raw secret key into a byte array
//        byte[] keyBytes = SECRET_KEY.getBytes();
//        // Use Keys.hmacShaKeyFor to create a SecretKey for HS256
//        return Keys.hmacShaKeyFor(keyBytes);
//    }
//
//    // ✅ Extract the email (subject) from the JWT token
//    public String extractUserName(String token) {
//        return extractClaim(token, Claims::getSubject);
//    }
//
//    // ✅ Extract a specific claim from the JWT token
//    private <T> T extractClaim(String token, Function<Claims, T> claimResolver) {
//        final Claims claims = extractAllClaims(token);
//        return claimResolver.apply(claims);
//    }
//
//    // ✅ Extract all claims from the JWT token
//    private Claims extractAllClaims(String token) {
//        return Jwts.parser()
//                .setSigningKey(getKey()) // Use the same key for verification
//                .build()
//                .parseClaimsJws(token) // Parse and verify the token
//                .getBody(); // Return the claims
//    }
//
//    // ✅ Validate the JWT token
//    public boolean validateToken(String token, UserDetails userDetails) {
//        final String userEmail = extractUserName(token);
//        return (userEmail.equals(userDetails.getUsername()) && !isTokenExpired(token));
//    }
//
//    // ✅ Check if the token is expired
//    private boolean isTokenExpired(String token) {
//        return extractExpiration(token).before(new Date());
//    }
//
//    // ✅ Extract the expiration date from the JWT token
//    private Date extractExpiration(String token) {
//        return extractClaim(token, Claims::getExpiration);
//    }
//}