package com.springsecurity.utility;
import com.springsecurity.entity.User;
import com.springsecurity.type.ProviderType;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
@Slf4j
@Component
public class AuthUtility {

    // Secret key for signing JWT (You should keep this in application.properties or env variables)
    @Value("${secret}")
    private  String SECRET_KEY ;

    // Token validity: e.g. 10 hours
    private final long JWT_EXPIRATION = 1000 * 60 * 5;


    private Key getSigningKey() {
        return Keys.hmacShaKeyFor(SECRET_KEY.getBytes());
    }

    // ✅ Extract username from token
    public String extractUsername(String token) {
        return extractAllClaims(token).getSubject();
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parser()
                .setSigningKey(getSigningKey())  // set your signing key
                .parseClaimsJws(token)     // parse the token
                .getBody();                // get the claims
    }

    // ✅ Extract expiration from token
    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    // ✅ Extract single claim
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    // ✅ Generate token for user
//    public String generateToken(UserDetails userDetails) {
//        return generateToken(new HashMap<>(), userDetails);
//    }

    // ✅ Generate token with extra claims
    public String generateToken(User user) {
        return Jwts.builder()
                .setSubject(user.getUsername())                // subject → usually the username/email
                .claim("userId", user.getUserId())             // custom claim
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + JWT_EXPIRATION))
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    // ✅ Validate token
    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername())) && !isTokenExpired(token);
    }

    // ✅ Check if token expired
    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    // ✅ Extract all claims
//    private Claims extractAllClaims(String token) {
//        return Jwts.parserBuilder()
//                .setSigningKey(getSigningKey())
//                .build()
//                .parseClaimsJws(token)
//                .getBody();
//    }


    public ProviderType getAuthProviderTypeFromRegistrationId(String registrationId) {
        if (registrationId == null || registrationId.isEmpty()) {
            throw new IllegalArgumentException("registrationId cannot be null or empty");
        }

        switch (registrationId.toLowerCase()) {
            case "google":
                return ProviderType.GOOGLE;
            case "github":
                return ProviderType.GITHUB;
            case "email":
                return ProviderType.EMAIL;
            default:
                throw new IllegalArgumentException("Unknown OAuth2 provider: " + registrationId);
        }
    }


    private static final Map<String, String> PROVIDER_ATTRIBUTE_MAP = Map.of(
            "google", "sub",
            "github", "id"
            // Add more providers here, e.g.,
            // "facebook", "id",
            // "linkedin", "id"
    );

    public  String getProviderIdFromOAuth2User(OAuth2User oAuth2User, String registrationId) {
        String providerKey = registrationId.toLowerCase();

        if (!PROVIDER_ATTRIBUTE_MAP.containsKey(providerKey)) {
            log.error("Unsupported OAuth2 provider: {}", registrationId);
            throw new IllegalArgumentException("Unsupported OAuth2 provider: " + registrationId);
        }

        String attributeName = PROVIDER_ATTRIBUTE_MAP.get(providerKey);
        Object providerIdObj = oAuth2User.getAttribute(attributeName);

        if (providerIdObj == null || providerIdObj.toString().isBlank()) {
            log.error("Unable to determine providerId for provider: {}", registrationId);
            throw new IllegalArgumentException("Unable to determine providerId for OAuth2 login");
        }

        return providerIdObj.toString();
    }



    public  Map<String, String> extractNameAndEmail(OAuth2User oAuth2User, String registrationId) {
        String provider = registrationId.toLowerCase();

        String name;
        String email;

        switch (provider) {
            case "google":
                name = oAuth2User.getAttribute("name");
                email = oAuth2User.getAttribute("email");
                break;

            case "github":
                name = oAuth2User.getAttribute("name");
                // GitHub sometimes returns null if email is private, fallback to login
                email = oAuth2User.getAttribute("email");
                if (email == null || email.isBlank()) {
                    email = oAuth2User.getAttribute("login") + "@github.com";
                }
                break;

            default:
                throw new IllegalArgumentException("Unsupported OAuth2 provider: " + registrationId);
        }

        if (name == null || name.isBlank() || email == null || email.isBlank()) {
            throw new IllegalArgumentException("Unable to extract name or email from OAuth2User for provider: " + registrationId);
        }

        return Map.of(
                "name", name,
                "email", email
        );
    }
}
