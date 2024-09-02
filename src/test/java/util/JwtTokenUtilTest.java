package util;

import com.majwic.exception.UnauthorizedException;
import com.majwic.util.JwtTokenUtil;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class JwtTokenUtilTest {

    @InjectMocks
    private JwtTokenUtil jwtTokenUtil;

    @BeforeEach
    public void setUp() {
        ReflectionTestUtils.setField(jwtTokenUtil, "secretKey", "mySecretKey");
        ReflectionTestUtils.setField(jwtTokenUtil, "expirationTime", 3600000L);
    }

    @Test
    public void testGenerateToken() {
        String userId = "testUser";
        String token = jwtTokenUtil.generateToken(userId);

        assertNotNull(token);
        String parsedUserId = Jwts.parser()
            .setSigningKey("mySecretKey")
            .parseClaimsJws(token)
            .getBody()
            .getSubject();
        assertEquals(userId, parsedUserId);
    }

    @Test
    public void testGetUserIdFromToken() {
        String userId = "testUser";
        String token = Jwts.builder()
            .setSubject(userId)
            .setIssuedAt(new Date())
            .setExpiration(new Date(System.currentTimeMillis() + 3600000L))
            .signWith(SignatureAlgorithm.HS256, "mySecretKey")
            .compact();

        String extractedUserId = jwtTokenUtil.getUserIdFromToken(token);
        assertEquals(userId, extractedUserId);
    }

    @Test
    public void testGetUserIdFromInvalidToken() {
        String invalidToken = "invalidToken";

        Exception exception = assertThrows(UnauthorizedException.class, () -> {
            jwtTokenUtil.getUserIdFromToken(invalidToken);
        });

        String expectedMessage = "Session token is invalid";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    public void testTokenExpiration() {
        String userId = "testUser";
        String token = Jwts.builder()
            .setSubject(userId)
            .setIssuedAt(new Date())
            .setExpiration(new Date(System.currentTimeMillis() - 1000L)) // expired token
            .signWith(SignatureAlgorithm.HS256, "mySecretKey")
            .compact();

        Exception exception = assertThrows(UnauthorizedException.class, () -> {
            jwtTokenUtil.getUserIdFromToken(token);
        });

        String expectedMessage = "Session token is invalid";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }
}
