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

@Tag(name = "Post", description = "Handles post operations including: creating, reading, updating, and deleting.")
public interface PostDocumentation {

    // ======================================== POST Create Post ======================================== //

    @Operation(
        summary = "Post Create Post",
        description = "Create post using request body"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Post created successfully",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(type = "String"),
                examples = @ExampleObject("{\"id\":6,\"content\":\"Example post content\",\"likes\":0," +
                    "\"dislikes\":0,\"tags\":[\"tag1\",\"tag2\",\"tag3\"],\"authorId\":2}")
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
                    @ExampleObject(name = "Content Length", value = "{\"error\":\"NOT_ACCEPTABLE\",\"message\":" +
                        "\"The 'content' field must be less than 1000 characters\",\"timestamp\":" +
                        "\"2024-08-26T22:20:21.252888200\"}"),
                    @ExampleObject(name = "Too many tags", value = "{\"error\":\"NOT_ACCEPTABLE\",\"message\":" +
                        "\"The 'tags' field cannot be a list of more than 3 items\",\"timestamp\":" +
                        "\"2024-08-26T22:20:21.252888200\"}")
                }
            )
        )
    })
    ResponseEntity<String> create(
        @RequestBody(
            description = "Details of the post to be created<br><br> content: required<br> tags: required (max of 3)",
            required = true,
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(type = "String"),
                examples = @ExampleObject("{\"content\":\"Example post content\",\"tags\":" +
                    "[\"tag1\",\"tag2\",\"tag3\"]}")
            )
        ) Map<String, Object> requestBody,
        HttpServletRequest request
    );

    // ======================================== POST React to Post ======================================== //

    @Operation(
        summary = "Post React to Post",
        description = "Create a reaction to a post using ID path variable and likeVal request parameter<br><br>" +
            "isLiked omitted: remove reaction<br> isLiked of true: like post<br> isLiked of false: dislike post"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Reaction to post created successfully",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(type = "String"),
                examples = @ExampleObject(value = "{\"id\":6,\"content\":\"Example post content\",\"likes\":1," +
                    "\"dislikes\":0,\"tags\":[\"tag1\",\"tag2\",\"tag3\"],\"authorId\":2,\"isLiked\":true}")
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
                examples = {
                    @ExampleObject(name = "Post", value = "{\"error\":\"NOT_FOUND\",\"message\":" +
                        "\"Post not found\",\"timestamp\":\"2024-08-26T22:20:21.252888200\"}"),
                    @ExampleObject(name = "Profile", value = "{\"error\":\"NOT_FOUND\",\"message\":" +
                        "\"Profile not found\",\"timestamp\":\"2024-08-26T22:20:21.252888200\"}")
                }
            )
        )
    })
    ResponseEntity<String> reactToPost(
        @PathVariable Long id,
        @RequestParam(required = false) Boolean likeVal,
        HttpServletRequest request
    );

    // ======================================== GET Post ======================================== //

    @Operation(
        summary = "Get Post",
        description = "Read post using ID from path"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Post read successfully",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(type = "String"),
                examples = @ExampleObject(value = "{\"id\":6,\"content\":\"Example post content\",\"likes\":1," +
                    "\"dislikes\":0,\"tags\":[\"tag1\",\"tag2\",\"tag3\"],\"authorId\":2,\"isLiked\":true}")
            )
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Not Found",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(type = "String"),
                examples = @ExampleObject(value = "{\"error\":\"NOT_FOUND\",\"message\":" +
                    "\"Post not found\",\"timestamp\":\"2024-08-26T22:20:21.252888200\"}")

            )
        )
    })
    ResponseEntity<String> read(
        @PathVariable Long id,
        HttpServletRequest request
    );

    // ======================================== GET Posts by Tag ======================================== //

    @Operation(
        summary = "Get Posts by Tag",
        description = "Read posts by tag and other specified request parameters<br><br>" +
            "tag: specify a tag<br>" +
            "sorBy: specify sorting by likes or dislike<br>" +
            "sortDir: specify sorting direction (asc or desc)<br>" +
            "page: specify what page to retrieve<br>" +
            "size: specify page size (max 20)"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Posts read successfully",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(type = "String"),
                examples = @ExampleObject("{\"posts\":[{\"id\":6,\"content\":\"Example post content\"," +
                    "\"likes\":1,\"dislikes\":0,\"tags\":[\"tag1\",\"tag2\",\"tag3\"],\"authorId\":2," +
                    "\"isLiked\":true},{\"id\":7,\"content\":\"Example post content\",\"likes\":0," +
                    "\"dislikes\":1,\"tags\":[\"tag1\",\"tag2\",\"tag3\"],\"authorId\":2," +
                    "\"isLiked\":false},{\"id\":8,\"content\":\"Example post content\"," +
                    "\"likes\":0,\"dislikes\":0,\"tags\":[\"tag1\",\"tag2\",\"tag3\"],\"authorId\":2}]}")
            )
        )
    })
    ResponseEntity<String> readAllByTag(
        @RequestParam(required = false) String tag,
        @RequestParam(defaultValue = "likes") String sortBy,
        @RequestParam(defaultValue = "desc") String sortDir,
        @RequestParam int page,
        @RequestParam int size,
        HttpServletRequest httpServletRequest
    );

    // ======================================== DELETE Post ======================================== //

    @Operation(
        summary = "Delete post",
        description = "Delete post using ID from path"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Deleted post successfully"
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
                    @ExampleObject(name = "Not Author", value = "{\"error\":\"UNAUTHORIZED\",\"message\":" +
                        "\"Post does not belong to profile\",\"timestamp\":\"2024-08-26T22:20:21.252888200\"}")
                }
            )
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Not Found",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(type = "String"),
                examples = @ExampleObject(value = "{\"error\":\"NOT_FOUND\",\"message\":" +
                    "\"Post not found\",\"timestamp\":\"2024-08-26T22:20:21.252888200\"}")

            )
        )
    })
    ResponseEntity<Void> delete(
        @PathVariable Long id,
        HttpServletRequest request
    );
}
