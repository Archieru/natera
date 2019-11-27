package com.bar42.natera;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.Matchers.*;

public class RateLimitingTest extends Base
{
    @Before
    public void beforeTest()
    { cleanUp(); }
    
    @After
    public void afterTest()
    { cleanUp(); }
    
    @Test
    public void only10TrianglesAvailable() // "You can save up to 10 triangles"
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
