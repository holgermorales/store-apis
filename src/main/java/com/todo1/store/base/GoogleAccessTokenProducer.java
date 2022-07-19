package com.todo1.store.base;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.NotAuthorizedException;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.PreMatching;
import javax.ws.rs.ext.Provider;
import java.io.IOException;
import java.util.Collections;

@Provider
@PreMatching
public class GoogleAccessTokenProducer implements ContainerRequestFilter {

    @Inject
    private HttpServletRequest request;

    /**
     * Valida el token con google identifier id.
     * @param containerRequestContext
     * @throws IOException
     */
    @Override
    public void filter(ContainerRequestContext containerRequestContext) throws IOException {
        String idTokenString = this.request.getHeader("Authorization");
        if (null == idTokenString) {
            throw new NotAuthorizedException("Invalid token");
        }
        HttpTransport transport = new NetHttpTransport();
        JsonFactory jsonFactory = new GsonFactory();
        GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(transport, jsonFactory)
                .setAudience(Collections.singletonList("keypublic"))
                .build();
        try {
            GoogleIdToken token = verifier.verify(idTokenString);
            if (null == token) {
                throw new NotAuthorizedException("Invalid token");
            }
        } catch (final Exception e) {
            throw new NotAuthorizedException("Invalid token");
        }
    }
}
