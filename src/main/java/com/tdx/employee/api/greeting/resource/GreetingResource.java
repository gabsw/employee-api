package com.tdx.employee.api.greeting.resource;

import io.quarkus.security.Authenticated;
import org.eclipse.microprofile.jwt.JsonWebToken;

import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@RequestScoped
@Path("/hello")
public class GreetingResource {
    @Inject
    JsonWebToken jwt;

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    @Path("/")
    @PermitAll
    public String helloAll() {
        return "Hello, world";
    }

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    @Path("/secured")
    @RolesAllowed("Subscriber")
    public String helloSecured() {
        return "Hello, " + jwt.getName() + ", you are part of the " + jwt.getGroups() + " group";
    }
}