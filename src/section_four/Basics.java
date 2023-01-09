package section_four;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.testng.Assert;

import files.ReUsableMethods;
import files.payload;

public class Basics {

	public static void main(String[] args) throws IOException {
		
		//Add place
		RestAssured.baseURI = "https://rahulshettyacademy.com";
		String response = given().log().all().queryParam("key", "qaclick123").header("Content-Type","application/json")
		.body(new String(Files.readAllBytes(Paths.get("E:\\Rest Assured\\rahul shetty\\practise API\\AddPlace.json"))))
		.when().post("maps/api/place/add/json")
		.then().assertThat().statusCode(200).body("scope",equalTo("APP"))
		.header("Server", "Apache/2.4.41 (Ubuntu)").extract().response().asString();
		
		System.out.println(response);     //run by commenting and after removing commenting
		JsonPath js = new JsonPath(response);		//for parsing Json response
		String placeID = js.getString("place_id");
		System.out.println(placeID);
		
		//Update place
		String newAdress = "Nirmal Township";
		given().log().all().queryParam("key", "qaclick123").header("Content-Type", "application/json")
		.body("{\r\n"
				+ "    \"place_id\": \""+placeID+"\",\r\n"
				+ "    \"address\": \""+newAdress+"\",\r\n"
				+ "    \"key\": \"qaclick123\"\r\n"
				+ "}")
		.when().put("maps/api/place/update/json")
		.then().log().all().assertThat().statusCode(200).body("msg", equalTo("Address successfully updated"));
		
		//Get Place
		String responseGetPlace = given().log().all().queryParam("key", "qaclick123").queryParam("place_id", placeID)
		.when().get("maps/api/place/get/json")
		.then().log().all().assertThat().statusCode(200).extract().response().asString();
				
		//JsonPath js1 = new JsonPath(responseGetPlace);
		JsonPath js1 = ReUsableMethods.rawToJson(responseGetPlace);
		String actualAddress = js1.getString("address");
		System.out.println(actualAddress);
		Assert.assertEquals(actualAddress, newAdress);
	}

}
