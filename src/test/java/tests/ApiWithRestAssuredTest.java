package tests;

import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.get;
import static io.restassured.RestAssured.given;
import static io.restassured.http.ContentType.JSON;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.hamcrest.core.Is.is;

public class ApiWithRestAssuredTest {

    @BeforeAll
    static void openUrl() {
        RestAssured.baseURI = "https://reqres.in/";
    }

    @Test
    @DisplayName("List users")
    void listUser() {
        String responseDataListUser = "{\"page\":2,\"per_page\":6,\"total\":12,\"total_pages\":2,\"data\":[{\"id\":7,\"email\":\"michael.lawson@reqres.in\",\"first_name\":\"Michael\",\"last_name\":\"Lawson\",\"avatar\":\"https://reqres.in/img/faces/7-image.jpg\"},{\"id\":8,\"email\":\"lindsay.ferguson@reqres.in\",\"first_name\":\"Lindsay\",\"last_name\":\"Ferguson\",\"avatar\":\"https://reqres.in/img/faces/8-image.jpg\"},{\"id\":9,\"email\":\"tobias.funke@reqres.in\",\"first_name\":\"Tobias\",\"last_name\":\"Funke\",\"avatar\":\"https://reqres.in/img/faces/9-image.jpg\"},{\"id\":10,\"email\":\"byron.fields@reqres.in\",\"first_name\":\"Byron\",\"last_name\":\"Fields\",\"avatar\":\"https://reqres.in/img/faces/10-image.jpg\"},{\"id\":11,\"email\":\"george.edwards@reqres.in\",\"first_name\":\"George\",\"last_name\":\"Edwards\",\"avatar\":\"https://reqres.in/img/faces/11-image.jpg\"},{\"id\":12,\"email\":\"rachel.howell@reqres.in\",\"first_name\":\"Rachel\",\"last_name\":\"Howell\",\"avatar\":\"https://reqres.in/img/faces/12-image.jpg\"}],\"support\":{\"url\":\"https://reqres.in/#support-heading\",\"text\":\"To keep ReqRes free, contributions towards server costs are appreciated!\"}}";
        String response =
                get("/api/users?page=2")
                        .then()
                        .statusCode(200)
                        .extract().response().asString();
        assertThat(response).isEqualTo(responseDataListUser);
    }

    @Test
    @DisplayName("Single user")
    void singleUser() {
        given()
                .when()
                .get("/api/users/2")
                .then()
                .statusCode(200)
                .body("data.id", is(2), "data.email", is("janet.weaver@reqres.in"),
                        "data.first_name", is("Janet"),
                        "data.last_name", is("Weaver"),
                        "data.avatar", is("https://reqres.in/img/faces/2-image.jpg"),
                        "support.url", is("https://reqres.in/#support-heading"),
                        "support.text", is("To keep ReqRes free, contributions towards server costs are appreciated!"));
    }

    @Test
    @DisplayName("Single user not found")
    public void singleUserNotFound() {
        given()
                .when()
                .get("/api/users/23")
                .then()
                .statusCode(404)
                .body(is("{}"));
    }

    @Test
    @DisplayName("Create")
    public void createUser() {
        given()
                .contentType(JSON)
                .body("{\"name\": \"morpheus\",\"job\": \"leader\"\n}")
                .when()
                .post("/api/users")
                .then()
                .statusCode(201)
                .body("name", is("morpheus"), "job", is("leader"));
    }

    @Test
    @DisplayName("Register Successful")
    public void registerSuccessful() {
        given()
                .contentType(JSON)
                .body("{\"email\": \"eve.holt@reqres.in\",\"password\": \"pistol\"}")
                .when()
                .post("/api/register")
                .then()
                .statusCode(200)
                .body("id", is(4), "token", is("QpwL5tke4Pnpja7X4"));
    }

    @Test
    @DisplayName("List <resource>")
    public void listResource() {
        given()
                .when()
                .get("/api/unknown")
                .then()
                .statusCode(200)
                .body("page", is(1), "per_page", is(6), "total", is(12), "total_pages", is(2));
    }
}
