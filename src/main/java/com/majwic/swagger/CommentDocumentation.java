package com.majwic.swagger;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
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

@Tag(name = "Comment", description = "Handles comment operations including: creating, reading, updating, and deleting")
public interface CommentDocumentation {

    // ======================================== POST Create Comment ======================================== //

    @Operation(
        summary = "Post Create Comment",
        description = "Create comment using request body"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200",
            description = "Comment created successfully",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(type = "String"),
                examples = {
                    @ExampleObject(name = "Without Parent", value = "{\"id\":1,\"content\":" +
                        "\"Example comment content\",\"likes\":0,\"dislikes\":0,\"postId\":6,\"authorId\":2}"),
                    @ExampleObject(name = "With Parent", value = "{\"id\":2,\"content\":\"Example comment content\"," +
                        "\"likes\":0,\"dislikes\":0,\"postId\":6,\"authorId\":2,\"parentCommentId\":1}")
                }
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
                    @ExampleObject(name = "Profile", value = "{\"error\":\"NOT_FOUND\",\"message\":" +
                        "\"Profile not found\",\"timestamp\":\"2024-08-26T22:20:21.252888200\"}"),
                    @ExampleObject(name = "Post", value = "{\"error\":\"NOT_FOUND\",\"message\":" +
                        "\"Post not found\",\"timestamp\":\"2024-08-26T22:20:21.252888200\"}")
                }
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
                        "\"2024-08-26T22:20:21.252888200\"}")
                }
            )
        )
    })
    ResponseEntity<String> create(
        @RequestBody(
            description = "Details of the comment to be created<br><br> content: required<br> postId: required<br>" +
                "parentCommentId: optional (required if comment to a comment)",
            required = true,
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(type = "String"),
                examples = {
                    @ExampleObject(name = "Without Parent", value = "{\"content\":\"Example comment content\"," +
                        "\"postId\":\"6\"}"),
                    @ExampleObject(name = "With Parent", value = "{\"content\":\"Example comment content\"," +
                        "\"postId\":\"6\",\"parentCommentId\":1}")
                }
            )
        )
        Map<String, Object> requestBody,
        @Parameter(hidden = true) HttpServletRequest request
    );

    // ======================================== POST React to Comment ======================================== //

    @Operation(
        summary = "Post React to Comment",
        description = "Create a reaction to a comment using ID path variable and likeVal request parameter<br><br>" +
            "isLiked omitted: remove reaction<br> isLiked of true: like comment<br> isLiked of false: dislike comment"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Reaction to comment created successfully",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(type = "String"),
                examples = @ExampleObject("{\"id\":2,\"content\":\"Example comment content\"," +
                    "\"likes\":0,\"dislikes\":0,\"postId\":6,\"authorId\":2,\"parentCommentId\":1,\"isLiked\":true}")
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
                    @ExampleObject(name = "Comment", value = "{\"error\":\"NOT_FOUND\",\"message\":" +
                        "\"Post not found\",\"timestamp\":\"2024-08-26T22:20:21.252888200\"}"),
                    @ExampleObject(name = "Profile", value = "{\"error\":\"NOT_FOUND\",\"message\":" +
                        "\"Profile not found\",\"timestamp\":\"2024-08-26T22:20:21.252888200\"}")
                }
            )
        )
    })
    ResponseEntity<String> reactToComment(
        @PathVariable Long id,
        @RequestParam(required = false) Boolean likeVal,
        HttpServletRequest request
    );

    // ======================================== GET Comment ======================================== //

    @Operation(
        summary = "Get Comment",
        description = "Read comment using ID from path"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Comment read successfully",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(type = "String"),
                examples = @ExampleObject("{\"id\":2,\"content\":\"Example comment content\",\"likes\":0," +
                    "\"dislikes\":0,\"postId\":6,\"authorId\":2,\"parentCommentId\":1,\"isLiked\":true}")
            )
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Not Found",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(type = "String"),
                examples = @ExampleObject(value = "{\"error\":\"NOT_FOUND\",\"message\":" +
                    "\"Comment not found\",\"timestamp\":\"2024-08-26T22:20:21.252888200\"}")

            )
        )
    })
    ResponseEntity<String> read(
        @PathVariable Long id,
        HttpServletRequest request
    );

    // ======================================== GET Comments by Post ======================================== //

    @Operation(
        summary = "Get Comments by Post",
        description = "Read comments by postId path variable and other specified request parameters<br><br>" +
            "parentCommentID: optionally specify parentComment to search by<br>" +
            "page: specify what page to retrieve<br>" +
            "size: specify page size (max 20)"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Comments read successfully",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(type = "String"),
                examples = @ExampleObject("{\"comments\":[{\"id\":2,\"content\":\"Example comment content\"," +
                    "\"likes\":0,\"dislikes\":0,\"postId\":6,\"authorId\":2,\"parentCommentId\":1,\"isLiked\":true}," +
                    "{\"id\":3,\"content\":\"Example comment content\",\"likes\":0,\"dislikes\":0,\"postId\":6," +
                    "\"authorId\":2,\"parentCommentId\":1,\"isLiked\":false},{\"id\":4,\"content\":" +
                    "\"Example comment content\",\"likes\":0,\"dislikes\":0,\"postId\":6,\"authorId\":2," +
                    "\"parentCommentId\":1}]}")
            )
        )
    })
    ResponseEntity<String> readAll(
        @PathVariable Long postId,
        @RequestParam(required = false) Long parentCommentId,
        @RequestParam int page,
        @RequestParam int size,
        HttpServletRequest request
    );

    // ======================================== DELETE Comment ======================================== //

    @Operation(
        summary = "Delete Comment",
        description = "Delete comment using ID from path"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Deleted comment successfully"
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
                        "\"Comment does not belong to profile\",\"timestamp\":\"2024-08-26T22:20:21.252888200\"}")
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
                    "\"Comment not found\",\"timestamp\":\"2024-08-26T22:20:21.252888200\"}")

            )
        )
    })
    ResponseEntity<Void> delete(
        @PathVariable Long id,
        HttpServletRequest request
    );
}
