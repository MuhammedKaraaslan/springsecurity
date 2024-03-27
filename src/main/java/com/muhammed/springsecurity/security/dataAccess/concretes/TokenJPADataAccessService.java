package com.muhammed.springsecurity.security.dataAccess.concretes;

import com.muhammed.springsecurity.security.dataAccess.abstracts.TokenDao;
import com.muhammed.springsecurity.security.dataAccess.abstracts.TokenRepository;
import com.muhammed.springsecurity.security.model.entities.Token;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository("token-jpa")
@RequiredArgsConstructor
public class TokenJPADataAccessService implements TokenDao {

    private final TokenRepository tokenRepository;

    @Override
    public List<Token> findAllValidTokenByUser(Integer userId) {
        return this.tokenRepository.findAllValidTokenByUser(userId);
    }

    @Override
    public Optional<Token> findByToken(String token) {
        return this.tokenRepository.findByToken(token);
    }

    @Override
    public Token save(Token token) {
        return this.tokenRepository.save(token);
    }
}
