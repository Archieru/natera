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
        responseShouldHave // {"id":"...","firstSide":3.0,"secondSide":4.0,"thirdSide":5.0}
            .statusCode(200)
            .body("id", is(not(emptyString())))
            .body("firstSide", equalTo(3.0f))
            .body("secondSide", equalTo(4.0f))
            .body("thirdSide", equalTo(5.0f));
    }

    @Test
    public void negativeTriangle()
    {
        ValidatableResponse responseShouldHave = addTriangle(-3,4,5);
        responseShouldHave // {"id":"...","firstSide":3.0,"secondSide":4.0,"thirdSide":5.0}
            .statusCode(200)
            .body("id", is(not(emptyString())))
            .body("firstSide", equalTo(3.0f))
            .body("secondSide", equalTo(4.0f))
            .body("thirdSide", equalTo(5.0f));
    }

    @Test
    public void zeroTriangle()
    {
        ValidatableResponse responseShouldHave = addTriangle(0,4,5);
        responseShouldHave // {"timestamp": ...,"status":422,"error":"Unprocessable Entity","exception":"com.natera.test.triangle.exception.UnprocessableDataException","message":"Cannot process input","path":"/triangle"}
            .statusCode(422)
            .body("id", is(not(emptyString())))
            .body("error", equalTo("Unprocessable Entity"))
            .body("message", equalTo("Cannot process input"));
    }

    @Test
    public void lineTriangle()
    {
        ValidatableResponse responseShouldHave = addTriangle(2,3,5);
        responseShouldHave // {"timestamp": ...,"status":422,"error":"Unprocessable Entity","exception":"com.natera.test.triangle.exception.UnprocessableDataException","message":"Cannot process input","path":"/triangle"}
            .statusCode(422)
            .body("id", is(not(emptyString())))
            .body("error", equalTo("Unprocessable Entity"))
            .body("message", equalTo("Cannot process input"));
    }

    @Test
    public void impossibleTriangle()
    {
        ValidatableResponse responseShouldHave = addTriangle(3,4,10);
        responseShouldHave // {"timestamp": ...,"status":422,"error":"Unprocessable Entity","exception":"com.natera.test.triangle.exception.UnprocessableDataException","message":"Cannot process input","path":"/triangle"}
            .statusCode(422)
            .body("id", is(not(emptyString())))
            .body("error", equalTo("Unprocessable Entity"))
            .body("message", equalTo("Cannot process input"));
    }
}
