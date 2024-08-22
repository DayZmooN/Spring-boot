package com.example.demo.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

/**
 * Service pour la gestion des jetons JWT (JSON Web Token).
 * Permet de générer, valider et extraire des informations des jetons JWT.
 */
@Service
public class JwtService {

    /**
     * Clé secrète utilisée pour signer les jetons JWT.
     */
    @Value("${security.jwt.secret-key}")
    private String secretKey;

    /**
     * Temps d'expiration des jetons JWT en millisecondes.
     */
    @Value("${security.jwt.expiration-time}")
    private long jwtExpiration;

    /**
     * Extrait le nom d'utilisateur (subject) du jeton JWT.
     *
     * @param token le jeton JWT à analyser
     * @return le nom d'utilisateur extrait du jeton
     */
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    /**
     * Extrait une revendication spécifique (claim) du jeton JWT.
     *
     * @param <T> le type de la revendication à extraire
     * @param token le jeton JWT à analyser
     * @param claimsResolver une fonction qui spécifie la revendication à extraire
     * @return la valeur de la revendication extraite
     */
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    /**
     * Génère un jeton JWT pour un utilisateur donné.
     *
     * @param userDetails les détails de l'utilisateur pour lequel générer le jeton
     * @return le jeton JWT généré
     */
    public String generateToken(UserDetails userDetails) {
        return generateToken(new HashMap<>(), userDetails);
    }

    /**
     * Génère un jeton JWT avec des revendications supplémentaires pour un utilisateur donné.
     *
     * @param extraClaims revendications supplémentaires à inclure dans le jeton
     * @param userDetails les détails de l'utilisateur pour lequel générer le jeton
     * @return le jeton JWT généré
     */
    public String generateToken(Map<String, Object> extraClaims, UserDetails userDetails) {
        return buildToken(extraClaims, userDetails, jwtExpiration);
    }

    /**
     * Retourne le temps d'expiration configuré pour les jetons JWT.
     *
     * @return le temps d'expiration des jetons JWT en millisecondes
     */
    public long getExpirationTime() {
        return jwtExpiration;
    }

    /**
     * Construit un jeton JWT avec les revendications, les détails de l'utilisateur et l'expiration spécifiés.
     *
     * @param extraClaims revendications supplémentaires à inclure dans le jeton
     * @param userDetails les détails de l'utilisateur pour lequel générer le jeton
     * @param expiration temps d'expiration du jeton en millisecondes
     * @return le jeton JWT généré
     */
    private String buildToken(
            Map<String, Object> extraClaims,
            UserDetails userDetails,
            long expiration
    ) {
        return Jwts
                .builder()
                .setClaims(extraClaims)
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(getSignInKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    /**
     * Vérifie si un jeton JWT est valide pour un utilisateur donné.
     *
     * @param token le jeton JWT à valider
     * @param userDetails les détails de l'utilisateur contre lequel valider le jeton
     * @return vrai si le jeton est valide, faux sinon
     */
    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername())) && !isTokenExpired(token);
    }

    /**
     * Vérifie si un jeton JWT est expiré.
     *
     * @param token le jeton JWT à analyser
     * @return vrai si le jeton est expiré, faux sinon
     */
    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    /**
     * Extrait la date d'expiration du jeton JWT.
     *
     * @param token le jeton JWT à analyser
     * @return la date d'expiration du jeton
     */
    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    /**
     * Extrait toutes les revendications (claims) d'un jeton JWT.
     *
     * @param token le jeton JWT à analyser
     * @return les revendications extraites du jeton
     */
    private Claims extractAllClaims(String token) {
        return Jwts
                .parserBuilder()
                .setSigningKey(getSignInKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    /**
     * Retourne la clé utilisée pour signer les jetons JWT.
     *
     * @return la clé de signature
     */
    private Key getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
