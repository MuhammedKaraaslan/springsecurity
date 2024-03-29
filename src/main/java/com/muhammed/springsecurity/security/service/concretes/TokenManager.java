package com.muhammed.springsecurity.security.service.concretes;

import com.muhammed.springsecurity.security.dataAccess.abstracts.TokenDao;
import com.muhammed.springsecurity.security.model.entities.Token;
import com.muhammed.springsecurity.security.service.abstracts.TokenService;
import org.springframework.beans.factory.annotation.Qualifier;

import java.util.List;
import java.util.Optional;

public class TokenManager implements TokenService {

    private final TokenDao tokenDao;

    public TokenManager(@Qualifier("token-jpa") TokenDao tokenDao) {
        this.tokenDao = tokenDao;
    }

    @Override
    public List<Token> findAllValidTokenByUser(Integer userId) {
        return this.tokenDao.findAllValidTokenByUser(userId);
    }

    @Override
    public Optional<Token> findByToken(String token) {
        return this.tokenDao.findByToken(token);
    }

    @Override
    public Token save(Token token) {
        return this.tokenDao.save(token);
    }

    @Override
    public void saveAll(List<Token> validUserTokens) {
        this.tokenDao.saveAll(validUserTokens);
    }
}
