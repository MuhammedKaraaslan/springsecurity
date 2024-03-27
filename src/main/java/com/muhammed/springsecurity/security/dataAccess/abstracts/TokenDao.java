package com.muhammed.springsecurity.security.dataAccess.abstracts;

import com.muhammed.springsecurity.security.model.entities.Token;

public interface TokenDao {

    Token save(Token token);

}
