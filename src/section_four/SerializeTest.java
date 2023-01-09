package section_four;

import io.restassured.RestAssured;
import pojo.AddPlaces;
import pojo.Location;

import static io.restassured.RestAssured.*;

import java.util.ArrayList;
import java.util.List;

public class SerializeTest {

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
		
		RestAssured.baseURI = "https://rahulshettyacademy.com";
		
		String res = given()
			.log().all()
			.queryParam("key", "qaclick123")
			.body(p)
		.when()
			.post("/maps/api/place/add/json")
		.then()
			.assertThat().statusCode(200)
			.extract().response().asString();
		
		System.out.println(res);

	}

}
