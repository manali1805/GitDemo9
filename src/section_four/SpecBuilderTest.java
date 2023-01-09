package section_four;

import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import pojo.AddPlaces;
import pojo.Location;

import static io.restassured.RestAssured.*;

import java.util.ArrayList;
import java.util.List;

public class SpecBuilderTest {

	public static void main(String[] args) {
		
		AddPlaces p = new AddPlaces();
		p.setAccuracy(50);
		p.setName("Manali Kulkarni");
		p.setPhone_number("(+91) 983 893 3937");
		p.setAddress("29, side layout, cohen 09");
		p.setWebsite("http://manalikulkarni.com");
		p.setLanguage("French-IN");
		
		List<String> l = new ArrayList<String>();
		l.add("shoe park");
		l.add("shop");
		p.setTypes(l);
		
		Location l1 = new Location();
		l1.setLat(-38.383494);
		l1.setLng(33.427362);
		p.setLocation(l1);
		
		RequestSpecification req = new RequestSpecBuilder().setBaseUri("https://rahulshettyacademy.com")
		.setContentType(ContentType.JSON)
		.addQueryParam("key", "qaclick123").build();
		
		ResponseSpecification res = new ResponseSpecBuilder().expectStatusCode(200)
		.expectContentType(ContentType.JSON).build();
		
		//RestAssured.baseURI = "https://rahulshettyacademy.com";
		
		RequestSpecification req1 = given()
			.spec(req)
			.body(p);
		
		String response = req1
			.when()
				.post("/maps/api/place/add/json")
			.then()
				.spec(res)
				.extract().response().asString();
		
		System.out.println(response);

	}

}
