# Foro Hub
This project is a REST API to manage topics in a forum.

This is a challenge project.

## How to run
1. Clone the repository.
2. Create a PostgreSQL database.
3. Set the database connection in the `application.properties` file.
4. Install the project dependencies with the command `mvn install`.
5. Run the project with the command `mvn spring-boot:run`.
6. The API documentation will be available at `http://localhost:8080/swagger-ui.html`.

## Available endpoints
### Users
- `POST /auth/login`: Login with a user.

At this moment, there is no endpoint to create a user. The users are created directly in the database.

### Topics
- `GET /topics`: Get all topics with pagination.
- `GET /topics/{id}`: Get a topic by id.
- `POST /topics`: Create a new topic.
- `PUT /topics/{id}`: Update a topic by id.
- `DELETE /topics/{id}`: Delete a topic by id.
- `GET /ten-most-recent`: Get the ten most recent topics.

## Authentication
The API uses JWT to authenticate the users. To access the endpoints, you need to pass the token in the header of the request.

## Database
The database is created with Flyway. The migration files are in the `resources/db.migration` folder.

## Tests
The tests are in the `src/test` folder. The project has unit tests for the services and controllers.

## Improvements
- Create an endpoint to create a user.
- Add integration tests.
- Add more validations.
- Add more error handling.
- Add more documentation.
- Add more security configurations.

## Technologies
- Kotlin: Main programming language.
- Spring Boot: Framework to create the REST API.
- PostgreSQL: Database to store the data.
- Maven: Dependency management.

## Author
- [LinkedIn](https://www.linkedin.com/in/denis-omar-cuyo-ttito-421a02334)
- [GitHub](https://github.com/OTF31)
