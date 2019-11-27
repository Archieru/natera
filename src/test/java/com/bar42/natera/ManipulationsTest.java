package com.bar42.natera;

import io.restassured.response.ValidatableResponse;
import org.junit.Assert;
import org.junit.Test;
import static org.hamcrest.Matchers.equalTo;

public class ManipulationsTest extends Base
{
    @Test
    public void listTrianglesTest()
    {
        String testId = getTestTriangleId();
        ValidatableResponse response = listTriangles();
        response.statusCode(200);
        Assert.assertTrue(response.extract().asString().contains(testId));
    }
    
    @Test
    public void getTriangleTest()
    {
        String testId = getTestTriangleId(3,4,5);
        ValidatableResponse response = getTriangle(testId);
        response
            .statusCode(200)
            .body("id", equalTo(testId))
            .body("firstSide", equalTo(3.0f))
            .body("secondSide", equalTo(4.0f))
            .body("thirdSide", equalTo(5.0f));
    }
    
    @Test
    public void getTriangleAreaTest()
    {
        String testId = getTestTriangleId();
        ValidatableResponse response = getTriangleArea(testId);
        response.statusCode(200);
        Assert.assertTrue(response.extract().asString().contains(testId));
    }
    
    @Test
    public void getTrianglePerimeterTest()
    {
        String testId = getTestTriangleId();
        ValidatableResponse response = getTrianglePerimeter(testId);
        response.statusCode(200);
        Assert.assertTrue(response.extract().asString().contains(testId));
    }
    
    @Test
    public void deleteTriangleTest()
    {
        String testId = getTestTriangleId();
        ValidatableResponse response = deleteTriangle(testId);
        response.statusCode(200);
        Assert.assertTrue(response.extract().asString().contains(testId));
        
        getTriangle(testId).statusCode(404);
    }
    
}
