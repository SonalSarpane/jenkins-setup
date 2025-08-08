package org.example.tests;

import io.restassured.http.ContentType;
import org.example.models.User;
import io.restassured.RestAssured;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

public class UserAPITests {
    // Base URL for the Reqres API
    private static final String BASE_URL = "https://reqres.in/api";

    @BeforeClass
    public void setup() {
        // Set the base URI for REST Assured
        baseURI = BASE_URL;
        // Add the x-api-key header to all requests
        RestAssured.requestSpecification = RestAssured.given()
            .header("x-api-key", "reqres-free-v1");
    }

    // Test to get a single user by ID
    @Test(description = "Get a single user")
    public void testGetSingleUser() {
        given()
            // Set path parameter 'id' to 2
            .pathParam("id", 2)
        .when()
            // Send GET request to /users/{id}
            .get("/users/{id}")
        .then()
            // Log response if validation fails
            .log().ifValidationFails()
            // Assert status code is 200
            .statusCode(200)
            // Assert 'data.id' is 2
            .body("data.id", equalTo(2))
            // Assert 'data.email' is not empty
            .body("data.email", not(emptyString()))
            // Assert 'data.first_name' is not empty
            .body("data.first_name", not(emptyString()))
            // Assert 'data.last_name' is not empty
            .body("data.last_name", not(emptyString()));
    }

    // Test to get a list of users
    @Test(description = "Get list of users")
    public void testGetUsersList() {
        given()
            // Set query parameter 'page' to 1
            .queryParam("page", 1)
        .when()
            // Send GET request to /users
            .get("/users")
        .then()
            // Log response if validation fails
            .log().ifValidationFails()
            // Assert status code is 200
            .statusCode(200)
            // Assert 'page' is 1
            .body("page", equalTo(1))
            // Assert 'data' array has at least one element
            .body("data", hasSize(greaterThan(0)))
            // Assert 'data[0].id' is not null
            .body("data[0].id", notNullValue());
    }

    // Test to create a new user
    @Test(description = "Create a new user")
    public void testCreateUser() {
        // Create a new User object
        User newUser = new User(
            "morpheus@reqres.in", // email
            "Morpheus",           // first name
            "Leader"              // job
        );

        given()
            // Set content type to JSON
            .contentType(ContentType.JSON)
            // Set request body to newUser object
            .body(newUser)
        .when()
            // Send POST request to /users
            .post("/users")
        .then()
            // Log response if validation fails
            .log().ifValidationFails()
            // Assert status code is 201
            .statusCode(201)
            // Assert 'id' is not null in response
            .body("id", notNullValue())
            // Assert 'createdAt' is not null in response
            .body("createdAt", notNullValue());
    }

    // Test to update an existing user
    @Test(description = "Update an existing user")
    public void testUpdateUser() {
        // Create an updated User object
        User updatedUser = new User(
            "morpheus@reqres.in", // email
            "Morpheus",           // first name
            "Captain"             // job
        );

        given()
            // Set content type to JSON
            .contentType(ContentType.JSON)
            // Set request body to updatedUser object
            .body(updatedUser)
            // Set path parameter 'id' to 2
            .pathParam("id", 2)
        .when()
            // Send PUT request to /users/{id}
            .put("/users/{id}")
        .then()
            // Log response if validation fails
            .log().ifValidationFails()
            // Assert status code is 200
            .statusCode(200)
            // Assert 'updatedAt' is not null in response
            .body("updatedAt", notNullValue());
    }

    // Test to delete a user
    @Test(description = "Delete a user")
    public void testDeleteUser() {
        given()
            // Set path parameter 'id' to 2
            .pathParam("id", 2)
        .when()
            // Send DELETE request to /users/{id}
            .delete("/users/{id}")
        .then()
            // Log response if validation fails
            .log().ifValidationFails()
            // Assert status code is 204
            .statusCode(204);
    }

    // Test to verify error response for invalid user
    @Test(description = "Verify error response for invalid user")
    public void testGetInvalidUser() {
        given()
            // Set path parameter 'id' to 23 (invalid user)
            .pathParam("id", 23)
        .when()
            // Send GET request to /users/{id}
            .get("/users/{id}")
        .then()
            // Log response if validation fails
            .log().ifValidationFails()
            // Assert status code is 404
            .statusCode(404);
    }
}
