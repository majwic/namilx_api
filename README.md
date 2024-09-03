# NamilxAPI
A RESTful API designed for a social media application. This API provides endpoints for profile management, authentication, and social interactions, including posting, commenting, and liking/disliking posts and comments.

## Features
- __Profile Management:__ Create, read, update, and delete profiles.
- __Authentication:__ Sign-in and sign-out with secure token-based authentication.
- __Posting:__ Create and manage posts.
- __Commenting:__ Add comments to posts and other comments.
- __Reactions:__ Like or dislike posts and comments.

## Getting Started
### Prerequisites
- Java 22
- Maven
- H2 Database (used by default for development and testing)
- An external database (optional, for production)

### Installation
1. Clone the repository:
```bash
git clone https://github.com/majwic/namilx_api.git
```
2. Navigate to the project directory:
```bash
cd namilx_api 
```
3. Build the project:
```bash
mvn clean install
```
4. Run the application:
```dtd
mvn spring-boot:run
```
The application will start on 'http://localhost:80' by default

### Configuration
Configuration properties can be found in 'src/main/resources/application.properties'. You can adjust settings such as JWT secrets, admin password, and database configurations here. By default, the application uses H2 for development and testing purposes. For production, you can configure an external database.

### API Endpoints
Profile Management
- __Create Profile:__ `POST /profile`
- __Get Profile By ID:__ `GET /profile/{id}`
- __Get Profile By Cookie:__ `GET /profile`
- __Update Profile:__ `PUT /profile`
- __Delete Profile:__ `DELETE /profile`

Authentication
- __Sign-in:__ `POST /auth/signin`
- __Validate Session:__ `GET /auth/validate`
- __Sign-out:__ `DELETE /auth/signin`

Post Management
- __Create Post:__ `POST /post`
- __Create Post Reaction:__ `POST /post/{id}`
- __Get Post:__ `GET /post/{id}`
- __Get Posts by Tag:__ `GET /post/tag`
- __Delete Post:__ `DELETE /post/{id}`

Comment Management
- __Create Comment:__ `POST /comment`
- __Create Comment Reaction:__ `POST /comment/{id}`
- __Get Comment:__ `GET /comment/{id}`
- __Get Comments from Post:__ `GET /comment/from-post/{postId}`
- __Delete Comment:__ `/comment/{id}`

### Running Tests
To run the tests: use the following command:
```bash
mvn test
```