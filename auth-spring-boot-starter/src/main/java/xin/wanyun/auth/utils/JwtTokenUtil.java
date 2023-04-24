package xin.wanyun.auth.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import xin.wanyun.auth.Guard;

import java.util.Date;
import java.util.UUID;

public class JwtTokenUtil {

    public static String createToken(String identity, Guard guard) {
        Algorithm algorithm = Algorithm.HMAC256(guard.getConfig().getSecret());
        Date current = new Date();
        Date expired = new Date();
        expired.setTime(current.getTime() + guard.getConfig().getExpired());
        return JWT.create()
                .withIssuer(guard.getPrefix())
                .withNotBefore(current)
                .withSubject(identity)
                .withIssuedAt(current)
                .withExpiresAt(expired)
                .withJWTId(UUID.randomUUID().toString())
                .sign(algorithm);
    }

    public static String verifyToken(String token, Guard guard) {
        Algorithm algorithm = Algorithm.HMAC256(guard.getConfig().getSecret());
        try {
            JWTVerifier verifier = JWT.require(algorithm).build();
            DecodedJWT verify = verifier.verify(token);
            return verify.getSubject();
        } catch (JWTVerificationException exception) {
            return null;
        }
    }

}
