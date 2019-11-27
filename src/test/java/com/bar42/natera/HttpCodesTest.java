package com.bar42.natera;

import org.junit.Test;

import static io.restassured.RestAssured.given;

public class HttpCodesTest extends Base
{
    @Test
    public void code415() // unknown charset
    {
        given()
            .header("X-User", "9f0ceee0-c482-4b59-b6ce-6c64841a72ff")
            .body("{3;4;5}")
            .post()
            .then()
            .statusCode(415);
    }
   
    @Test
    public void code422() // error in request
    {
        given()
            .header("X-User", "9f0ceee0-c482-4b59-b6ce-6c64841a72ff")
            .body("42")
            .post()
            .then()
            .statusCode(415);
    }
    
    @Test
    public void code404() // wrong path
    {
        given()
            .header("X-User", "9f0ceee0-c482-4b59-b6ce-6c64841a72ff")
            .body("{3;4;5}")
            .post("42")
            .then()
            .statusCode(404);
    }
    
    @Test
    public void code401() // no auth header
    {
        given()
            .body("{3;4;5}")
            .post()
            .then()
            .statusCode(401);
    }
}
