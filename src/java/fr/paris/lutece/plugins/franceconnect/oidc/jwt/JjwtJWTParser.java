/*
 * Copyright (c) 2002-2015, Mairie de Paris
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 *  1. Redistributions of source code must retain the above copyright notice
 *     and the following disclaimer.
 *
 *  2. Redistributions in binary form must reproduce the above copyright notice
 *     and the following disclaimer in the documentation and/or other materials
 *     provided with the distribution.
 *
 *  3. Neither the name of 'Mairie de Paris' nor 'Lutece' nor the names of its
 *     contributors may be used to endorse or promote products derived from
 *     this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDERS OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 *
 * License 1.0
 */
package fr.paris.lutece.plugins.franceconnect.oidc.jwt;

import fr.paris.lutece.plugins.franceconnect.oidc.AuthClientConf;
import fr.paris.lutece.plugins.franceconnect.oidc.AuthServerConf;
import fr.paris.lutece.plugins.franceconnect.oidc.IDToken;
import fr.paris.lutece.plugins.franceconnect.oidc.Token;
import fr.paris.lutece.plugins.franceconnect.web.Constants;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwt;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureException;

import org.apache.log4j.Logger;

import java.io.UnsupportedEncodingException;


/**
 * Jjwt JWTParser
 */
public class JjwtJWTParser implements JWTParser
{
    /**
     * {@inheritDoc }
     */
    @Override
    public void parseJWT( Token token, AuthClientConf clientConfig, AuthServerConf serverConfig, String strStoredNonce,
        Logger logger ) throws TokenValidationException
    {
        String strCompactJwt = token.getIdTokenString(  );

        try
        {
            JwtParser parser = Jwts.parser(  );
            parser.setSigningKey( clientConfig.getClientSecret(  ).getBytes( "UTF-8" ) );

            Jwt jwt = parser.parse( strCompactJwt );
            Claims claims = (Claims) jwt.getBody(  );

            IDToken idToken = new IDToken(  );
            idToken.setAudience( claims.getAudience(  ) );
            idToken.setIssuer( claims.getIssuer(  ) );
            idToken.setSubject( claims.getSubject(  ) );

            // Claims that should be verified
            idToken.setNonce( getVerifiedNonce( claims, strStoredNonce ) );
            idToken.setExpiration( getExpiration( claims ) );
            idToken.setIssueAt( getIssueAt( claims ) );

            // Extra claims for FranceConnect
            idToken.setIdProvider( (String) claims.get( Constants.CLAIM_IDP ) );
            idToken.setAcr( (String) claims.get( Constants.CLAIM_ACR ) );

            logger.debug( "ID Token retrieved by JJWT parser implementation : " + idToken );

            token.setIdToken( idToken );
        }
        catch ( SignatureException ex )
        {
            throw new TokenValidationException( ex.getMessage(  ), ex );
        }
        catch ( ExpiredJwtException ex )
        {
            throw new TokenValidationException( ex.getMessage(  ), ex );
        }
        catch ( UnsupportedEncodingException ex )
        {
            throw new TokenValidationException( ex.getMessage(  ), ex );
        }
    }

    /**
     * Retrieve and check the nonce
     * @param claims Claims set
     * @param strStoredNonce The stored nonce
     * @return The verified nonce
     * @throws TokenValidationException if the nonce is not valid
     */
    private String getVerifiedNonce( Claims claims, String strStoredNonce )
        throws TokenValidationException
    {
        // Check nonce
        String strNonce = (String) claims.get( Constants.CLAIM_NONCE );

        if ( strNonce == null )
        {
            throw new TokenValidationException( "The token doesn't contains the nonce info." );
        }

        if ( !strNonce.equals( strStoredNonce ) )
        {
            throw new TokenValidationException( "The nonce info has not the value expected." );
        }

        return strNonce;
    }

    /**
     * Retrieve the expiration date
     * @param claims Claims set
     * @return The expiration date
     */
    private String getExpiration( Claims claims )
    {
        long lExpiration = claims.getExpiration(  ).getTime(  );

        return String.valueOf( lExpiration / 1000L );
    }

    /**
     * Retrieve the issue at date
     * @param claims Claims set
     * @return The issue at date
     */
    private String getIssueAt( Claims claims )
    {
        long lIssueAt = claims.getIssuedAt(  ).getTime(  );

        return String.valueOf( lIssueAt / 1000L );
    }
}
