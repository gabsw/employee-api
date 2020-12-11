package com.tdx.employee.api;

/*import org.eclipse.microprofile.openapi.annotations.Components;
import org.eclipse.microprofile.openapi.annotations.OpenAPIDefinition;
import org.eclipse.microprofile.openapi.annotations.info.Contact;
import org.eclipse.microprofile.openapi.annotations.info.Info;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.parameters.Parameter;
import org.eclipse.microprofile.openapi.annotations.parameters.RequestBody;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;*/

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;

/*import static javax.ws.rs.core.MediaType.APPLICATION_JSON;
import static org.eclipse.microprofile.openapi.annotations.enums.ParameterIn.PATH;
import static org.eclipse.microprofile.openapi.annotations.enums.SchemaType.STRING;*/

@ApplicationPath(RestApplication.APP_PATH)
/*@OpenAPIDefinition(
    info = @Info(
        title = "BE Onboarding Project",
        version = "1.0",
        contact = @Contact(
            name = "Gabriela Santos",
            email = "ana.santos@talkdesk.com")
    ),
    components = @Components(
        parameters = {
            @Parameter(
                name = "id",
                description = "Id of the Employee to perform the operation",
                required = true,
                example = "20",
                in = PATH,
                schema = @Schema(type = STRING)
            )
        },
        requestBodies = {
            @RequestBody(
                name = "employeeDTO",
                description = "The Employee Object for writing operations",
                required = true,
                content = @Content(mediaType = APPLICATION_JSON)
            )
        },
        responses = {
            @APIResponse(
                name = "notFound",
                responseCode = "404",
                description = "Object Not found",
                content = @Content(mediaType = APPLICATION_JSON)
            ),
            @APIResponse(
                name = "internalError",
                responseCode = "500",
                description = "Internal Server Error",
                content = @Content(mediaType = APPLICATION_JSON)
            )
        }
    )
)*/
public class RestApplication extends Application {
    public static final String APP_PATH = "/api";
}
