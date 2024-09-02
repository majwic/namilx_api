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
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

@Tag(name = "Profile", description = "Handles user profile operations including: creating, reading, updating, " +
        "and deleting.")
public interface ProfileDocumentation {


    // ======================================== GET Profile by Cookie ======================================== //

    @Operation(
        summary = "Get Profile by Cookie",
        description = "Read public and private profile fields using JWT token from cookie."
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Profile read successful",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(type = "String"),
                examples = @ExampleObject("{\"id\":2,\"displayName\":\"example name\"," +
                    "\"email\":\"example@email.com\",\"roles\":[{\"id\":1,\"name\":\"USER\"}]}")
            )
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
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Not Found",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(type = "String"),
                examples = @ExampleObject(value = "{\"error\":\"NOT_FOUND\",\"message\":\"Profile not found\"," +
                    "\"timestamp\":\"2024-08-26T22:20:21.252888200\"}")
            )
        )
    })
    ResponseEntity<String> readByCookie(
        HttpServletRequest request
    );

    // ======================================== Get Profile by ID ======================================== //

    @Operation(
        summary = "Get Profile by ID",
        description = "Read public profile using ID from path"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200", description = "Profile read successfully", content = @Content(
                mediaType = "application/json",
                schema = @Schema(type = "String"),
                examples = @ExampleObject("{\"id\":2,\"displayName\":\"example name\"}")
            )
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Not Found",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(type = "String"),
                examples = @ExampleObject(value = "{\"error\":\"NOT_FOUND\",\"message\":\"Profile not found\"," +
                    "\"timestamp\":\"2024-08-26T22:20:21.252888200\"}")
            )
        )
    })
    ResponseEntity<String> readById(
        @PathVariable Long id
    );

    // ======================================== POST Create Profile ======================================== //

    @Operation(
        summary = "Post Create Profile",
        description = "Create profile using request body"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Profile created successfully",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(type = "String"),
                examples = @ExampleObject("{\"id\":2,\"displayName\":\"example name\"," +
                    "\"email\":\"example@email.com\",\"roles\":[{\"id\":1,\"name\":\"USER\"}]}")
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
                        "\"2024-08-26T22:20:21.252888200\"}"),
                    @ExampleObject(name = "Invalid Field Format", value = "{\"error\":\"NOT_ACCEPTABLE\",\"message\":" +
                        "\"Profile 'field' has invalid formatting\",\"timestamp\":\"2024-08-26T22:20:21.252888200\"}")
                }
            )
        ),
        @ApiResponse(
            responseCode = "409",
            description = "Conflict",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(type = "String"),
                examples = @ExampleObject(value = "{\"error\":\"CONFLICT\",\"message\":" +
                    "\"Profile already exists with email\",\"timestamp\":\"2024-08-26T22:20:21.252888200\"}")
            )
        )
    })
    ResponseEntity<String> create(
        @RequestBody(
            description = "Details of the profile to be created<br><br> email: required<br> password: required",
            required = true,
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(type = "String"),
                examples = @ExampleObject("{\"email\":\"example@email.com\",\"password\":\"Password1\"}")
            )
        )
        Map<String, Object> requestBody
    );

    // ======================================== PUT Update Profile ======================================== //

    @Operation(
        summary = "Put Update Profile",
        description = "Updates profile using request body"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Profile update successful",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(type = "String"),
                examples = @ExampleObject("{\"id\":2,\"displayName\":\"example name\"," +
                    "\"email\":\"example@email.com\",\"roles\":[{\"id\":1,\"name\":\"USER\"}]}")
            )
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
                        "\"message\":\"Session token is invalid\",\"timestamp\":\"2024-08-26T22:20:21.252888200\"}"),
                    @ExampleObject(name = "Incorrect Password", value = "{\"error\":\"UNAUTHORIZED\",\"message\":" +
                        "\"The 'currentPassword' is incorrect\",\"timestamp\":\"2024-08-26T22:20:21.252888200\"}")
                }
            )
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Not Found",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(type = "String"),
                examples = @ExampleObject(value = "{\"error\":\"NOT_FOUND\",\"message\":\"Profile not found\"," +
                    "\"timestamp\":\"2024-08-26T22:20:21.252888200\"}")
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
                        "\"2024-08-26T22:20:21.252888200\"}"),
                    @ExampleObject(name = "Invalid Field Format", value = "{\"error\":\"NOT_ACCEPTABLE\",\"message\":" +
                        "\"Profile 'field' has invalid formatting\",\"timestamp\":\"2024-08-26T22:20:21.252888200\"}")
                }
            )
        ),
        @ApiResponse(
            responseCode = "409",
            description = "Conflict",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(type = "String"),
                examples = @ExampleObject(value = "{\"error\":\"CONFLICT\",\"message\":" +
                    "\"Profile already exists with email\",\"timestamp\":\"2024-08-26T22:20:21.252888200\"}")
            )
        )
    })
    ResponseEntity<String> update(
        @RequestBody(
            description = "Details of update for a profile<br><br> " +
                "currentPassword: required<br>" +
                "displayName: optional<br>" +
                "email: optional<br>" +
                "newPassword: optional<br>",
            required = true,
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(type = "String"),
                examples = @ExampleObject("{\"currentPassword\":\"Password1\",\"displayName\":\"new name\"," +
                    "\"email\":\"example@newemail.com\",\"newPassword\":\"newPassword1\"}")
            )
        )
        Map<String, Object> requestBody,
        HttpServletRequest request
    );

    // ======================================== DELETE Profile ======================================== //

    @Operation(
        summary = "Delete Profile",
        description = "Deletes profiles using password from request parameter"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Profile deleted successfully"
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
                        "\"message\":\"Session token is invalid\",\"timestamp\":\"2024-08-26T22:20:21.252888200\"}"),
                    @ExampleObject(name = "Incorrect Password", value = "{\"error\":\"UNAUTHORIZED\",\"message\":" +
                        "\"Incorrect Password\",\"timestamp\":\"2024-08-26T22:20:21.252888200\"}")
                }
            )
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Not Found",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(type = "String"),
                examples = @ExampleObject(value = "{\"error\":\"NOT_FOUND\",\"message\":\"Profile not found\"," +
                    "\"timestamp\":\"2024-08-26T22:20:21.252888200\"}")
            )
        )
    })
    ResponseEntity<Void> delete(
        @RequestParam String password,
        HttpServletRequest request
    );
}
