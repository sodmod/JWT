package com.Security.JWT.Security.Services;

import com.Security.JWT.Exception.TokenRefreshException;
import com.Security.JWT.Repository.RefreshTokenRepository;
import com.Security.JWT.Repository.UserRepository;
import com.Security.JWT.models.RefreshToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Service
public class RefreshTokenService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RefreshTokenRepository refreshTokenRepository;
    @Value("${jwt.app.jwtRefreshExpirationMs}")
    private Long refreshTokenDurationMs;

    public Optional<RefreshToken> findByToken (String token) {
        return refreshTokenRepository.findByToken(token);
    }

    public RefreshToken createRefreshToken(Long userId) {
        RefreshToken refreshToken = new RefreshToken();

        refreshToken.setUser(userRepository.findById(userId).get());

        refreshToken.setExpiryDate(Instant.now().plusMillis(refreshTokenDurationMs));

        refreshToken.setToken(UUID.randomUUID().toString());

        refreshToken = refreshTokenRepository.save(refreshToken);

        return refreshToken;
    }

    public RefreshToken verifyExpiration(RefreshToken token){
        if(token.getExpiryDate().compareTo(Instant.now()) < 0) {
            refreshTokenRepository.delete(token);

            throw new TokenRefreshException(
                    token.getToken(),
                    "Refresh token was expired. Please make a new signin request"
            );
        }
        return token;
    }

    @Transactional
    public int deleteByUserId(Long userId) {
        return refreshTokenRepository.deleteByUser(
                userRepository.findById(
                        userId
                ).get()
        );
    }
}
