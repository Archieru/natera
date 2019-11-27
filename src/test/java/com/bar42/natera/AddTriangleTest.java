package com.bar42.natera;

import io.restassured.response.ValidatableResponse;
import org.junit.Test;

import static org.hamcrest.Matchers.*;

public class AddTriangleTest extends Base
{
    @Test
    public void basicTriangle()
    {
        ValidatableResponse responseShouldHave = addTriangle(3,4,5);
        responseShouldHave
            .statusCode(200)
            .body("id", is(not(emptyString())))
            .body("firstSide", equalTo(3.0f))
            .body("secondSide", equalTo(4.0f))
            .body("thirdSide", equalTo(5.0f));
    }
    
    @Test
    public void differentSeparator()
    {
        ValidatableResponse responseShouldHave = addTriangle(3,4,5, ",");
        responseShouldHave
            .statusCode(200)
            .body("id", is(not(emptyString())))
            .body("firstSide", equalTo(3.0f))
            .body("secondSide", equalTo(4.0f))
            .body("thirdSide", equalTo(5.0f));
    }
    
    @Test
    public void equalFloatTriangle()
    {
        ValidatableResponse responseShouldHave = addTriangle("{\"input\":\"4.2; 4.2; 4.2\"}");
        responseShouldHave
            .statusCode(200)
            .body("id", is(not(emptyString())))
            .body("firstSide", equalTo(4.2f))
            .body("secondSide", equalTo(4.2f))
            .body("thirdSide", equalTo(4.2f));
    }

    @Test
    public void negativeTriangle()
    {
        ValidatableResponse responseShouldHave = addTriangle(-3,4,5);
        responseShouldHave
            .statusCode(422)
            .body("id", is(not(emptyString())))
            .body("error", equalTo("Unprocessable Entity"))
            .body("message", equalTo("Cannot process input"));
    }

    @Test
    public void zeroTriangle()
    {
        ValidatableResponse responseShouldHave = addTriangle(0,4,5);
        responseShouldHave
            .statusCode(422)
            .body("id", is(not(emptyString())))
            .body("error", equalTo("Unprocessable Entity"))
            .body("message", equalTo("Cannot process input"));
    }

    @Test
    public void lineTriangle()
    {
        ValidatableResponse responseShouldHave = addTriangle(2,3,5);
        responseShouldHave
            .statusCode(422)
            .body("id", is(not(emptyString())))
            .body("error", equalTo("Unprocessable Entity"))
            .body("message", equalTo("Cannot process input"));
    }

    @Test
    public void impossibleTriangle()
    {
        ValidatableResponse responseShouldHave = addTriangle(3,4,10);
        responseShouldHave
            .statusCode(422)
            .body("id", is(not(emptyString())))
            .body("error", equalTo("Unprocessable Entity"))
            .body("message", equalTo("Cannot process input"));
    }
    
    @Test
    public void rateLimiting() // "You can save up to 10 triangles"
    {
        cleanUp();
        for (int i = 1; i <= 10; i++) addTriangle(3,4,5).statusCode(200);
        addTriangle(3, 4, 5) // should get error adding 11th triangle
            .statusCode(422)
            .body("id", is(not(emptyString())))
            .body("error", equalTo("Unprocessable Entity"))
            .body("message", equalTo("Limit exceeded"));;
        cleanUp();
    }
    
}
