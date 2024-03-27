package com.muhammed.springsecurity.security.dataAccess.concretes;

import com.muhammed.springsecurity.security.dataAccess.abstracts.TokenDao;
import com.muhammed.springsecurity.security.dataAccess.abstracts.TokenRepository;
import com.muhammed.springsecurity.security.model.entities.Token;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository("token-jpa")
@RequiredArgsConstructor
public class TokenJPADataAccessService implements TokenDao {

    private final TokenRepository tokenRepository;

    @Override
    public Token save(Token token) {
        return this.tokenRepository.save(token);
    }
}
