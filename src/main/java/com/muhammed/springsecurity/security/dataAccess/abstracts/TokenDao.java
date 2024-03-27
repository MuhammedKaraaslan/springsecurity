package com.muhammed.springsecurity.security.dataAccess.abstracts;

import com.muhammed.springsecurity.security.model.entities.Token;

import java.util.List;
import java.util.Optional;

public interface TokenDao {

    List<Token> findAllValidTokenByUser(Integer userId);

    Optional<Token> findByToken(String token);

    Token save(Token token);

}
