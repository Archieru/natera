package com.bar42.natera;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import io.restassured.RestAssured;
import io.restassured.response.ValidatableResponse;
import io.restassured.specification.RequestSpecification;
import org.json.JSONObject;
import org.junit.BeforeClass;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import static io.restassured.RestAssured.given;

public class Base
{
    @BeforeClass
    public static void setup()
    {
        RestAssured.baseURI = "https://qa-quiz.natera.com/triangle";
        cleanUp();
    }
    
    public static ValidatableResponse listTriangles()
    { return getBasicRequest().get("/all").then(); }
    
    public static ValidatableResponse getTriangle(String id)
    { return getBasicRequest().get("/" + id).then(); }
    
    public static ValidatableResponse getTriangleArea(String id)
    { return getBasicRequest().get("/" + id + "/area").then(); }
    
    public static ValidatableResponse getTrianglePerimeter(String id)
    { return getBasicRequest().get("/" + id + "/perimeter").then(); }
    
    public static ValidatableResponse deleteTriangle(String id)
    { return getBasicRequest().delete("/" + id).then(); }
    
    // For quiz purposes only.
    // Will never do that in production,
    // but shows that I'm able to do that
    public class Triangle { String id; float firstSide,  secondSide,  thirdSide; }
    public static void cleanUp()
    {
        ValidatableResponse response = listTriangles();
        response.statusCode(200);
        
        Type triangleListType = new TypeToken<List<Triangle>>() {}.getType();
        ArrayList<Triangle> allTriangles = new Gson().fromJson(response.extract().asString(), triangleListType);
        ArrayList<Thread> allTasks = new ArrayList<>();
        
        for (Triangle triangle : allTriangles)
        {
            Thread thread = new Thread(
                () -> deleteTriangle(triangle.id)
            );
            thread.start();
            allTasks.add(thread);
        }
        
        for (Thread thread : allTasks)
        { try { thread.join(); } catch (InterruptedException ignored) { } }
    }
    
    private static RequestSpecification getBasicRequest()
    { return given().header("X-User", "9f0ceee0-c482-4b59-b6ce-6c64841a72ff"); }
    
    
    public String getTestTriangleId()
    { return getTestTriangleId(3, 4, 5); }

    public String getTestTriangleId(int a, int b, int c)
    { return getTestTriangleId(addTriangle(a, b, c)); }
    
    public String getTestTriangleId(ValidatableResponse creationResponse)
    { return new JSONObject(creationResponse.extract().asString()).getString("id"); }
    
    
    public ValidatableResponse addTriangle(int a, int b, int c)
    { return addTriangle(a, b, c, null); }
    
    public ValidatableResponse addTriangle(int a, int b, int c, String separator)
    {
        JSONObject bodyJson = new JSONObject();
        String s = ";";
        if (separator != null)
        {
            s = separator;
            bodyJson.put("separator", s);
        }
        String input = a + s + b + s + c;
        bodyJson.put("input", input);
        
        return addTriangle(bodyJson.toString());
    }
    
    public ValidatableResponse addTriangle(String body)
    {
        return getBasicRequest()
            .header("Content-Type", "application/json;")
            .body(body)
            .post()
            .then();
    }
}
