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
package fr.paris.lutece.plugins.franceconnect.oidc.dataclient;

import fr.paris.lutece.plugins.franceconnect.oidc.AuthClientConf;
import fr.paris.lutece.plugins.franceconnect.oidc.AuthServerConf;
import fr.paris.lutece.plugins.franceconnect.oidc.Token;

import java.util.Set;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


/**
 *
 * @author pierre
 */
public interface DataClient
{
    /**
     * @return the DataServerUri
     */
    String getDataServerUri(  );

    /**
     * Returns the Name
     * @return The Name
     */
    String getName(  );

    /**
     * Returns the RedirectUri
     * @return The RedirectUri
     */
    String getRedirectUri(  );

    /**
     * Returns the Scope
     * @return The Scope
     */
    Set getScope(  );

    /**
     * Build a string that contains the list of scope separated by a plus
     * @return The scopes
     */
    String getScopes(  );

    /**
     * @return the TokenMethod
     */
    String getTokenMethod(  );

    /**
     * @param strDataServerUri the DataServerUri to set
     */
    void setDataServerUri( String strDataServerUri );

    /**
     * Sets the Name
     * @param strName The Name
     */
    void setName( String strName );

    /**
     * Sets the RedirectUri
     * @param strRedirectUri The RedirectUri
     */
    void setRedirectUri( String strRedirectUri );

    /**
     * Sets the Scope
     * @param scope The Scope
     */
    void setScope( Set scope );

    /**
     * @param strTokenMethod the TokenMethod to set
     */
    void setTokenMethod( String strTokenMethod );

    /**
     * Called by the callback
     * @param token
     * @param request The HTTP request
     * @param response The HTTP response
     */
    void handleToken( Token token , HttpServletRequest  request , HttpServletResponse  response );
}
