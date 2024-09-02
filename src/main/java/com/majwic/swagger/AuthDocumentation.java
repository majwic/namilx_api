package com.majwic.swagger;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;

import java.util.Map;

@Tag(name = "Auth", description = "Manages user sign-in, sign-out, and JWT token validation.")
public interface AuthDocumentation {

    // ======================================== Get Validate Session ======================================== //

    @Operation(
        summary = "Get Validate Session",
        description = "Validates the current session using JWT token from cookies"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Session is valid"
        ),
        @ApiResponse(
            responseCode = "401",
            description = "Unauthorized",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(type = "String"),
                examples = {
                    @ExampleObject(name = "Token Missing", value = "{\"error\":\"UNAUTHORIZED\"," +
                        "\"message\":\"Session token is missing\",\"timestamp\":\"2024-08-26T22:20:21.252888200\"}"),
                    @ExampleObject(name = "Token Invalid", value = "{\"error\":\"UNAUTHORIZED\"," +
                        "\"message\":\"Session token is invalid\",\"timestamp\":\"2024-08-26T22:20:21.252888200\"}")
                }
            )
        )
    })
    ResponseEntity<Void> validateSession(
        HttpServletRequest request
    );

    // ======================================== POST Sign-in ========================================

    @Operation(
        summary = "Post Sign-in",
        description = "Authorize profile sign-in using request body and adds JWT token to response cookie"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Sign-in successful"
        ),
        @ApiResponse(
            responseCode = "401",
            description = "Unauthorized",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(type = "String"),
                examples = @ExampleObject("{\"error\":\"UNAUTHORIZED\",\"message\":\"Incorrect Password\"," +
                    "\"timestamp\":\"2024-08-26T22:20:21.252888200\"}")
            )
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Not Found",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(type = "String"),
                examples = @ExampleObject("{\"error\":\"NOT_FOUND\",\"message\":" +
                    "\"Profile not found\",\"timestamp\":\"2024-08-26T22:20:21.252888200\"}")

            )
        ),
        @ApiResponse(
            responseCode = "406",
            description = "Not Acceptable",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(type = "String"),
                examples = {
                    @ExampleObject(name = "Field Missing", value = "{\"error\":\"NOT_ACCEPTABLE\",\"message\":" +
                        "\"The 'field name' field is required\",\"timestamp\":\"2024-08-26T22:20:21.252888200\"}"),
                    @ExampleObject(name = "Invalid Field Type", value = "{\"error\":\"NOT_ACCEPTABLE\",\"message\":" +
                        "\"The 'field name' field must be of type 'type name'\",\"timestamp\":" +
                        "\"2024-08-26T22:20:21.252888200\"}")
                }
            )
        )
    })
    ResponseEntity<Void> authProfile(
        @RequestBody(
            description = "Details of the sign-in fields<br><br> email: required<br> password: required",
            required = true,
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(type = "String"),
                examples = @ExampleObject("{\"email\":\"example@email.com\",\"password\":\"Password1\"}")
            )
        )
        Map<String, Object> requestBody,
        HttpServletResponse response
    );

    // ======================================== DELETE Sign-out ========================================

    @Operation(
        summary = "Delete Sign-out",
        description = "Add null and expired JWT token to response cookie"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Sign-out successful"
        )
    })
    ResponseEntity<Void> signout(
        HttpServletResponse response
    );
}
