package com.muhammed.springsecurity.token.dataAccess.abstracts;

import com.muhammed.springsecurity.token.model.entities.Token;

public interface TokenDao {

    Token save(Token token);

}
