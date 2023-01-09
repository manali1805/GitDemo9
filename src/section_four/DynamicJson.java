package section_four;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import files.ReUsableMethods;
import files.payload;
import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;

import static io.restassured.RestAssured.*;

public class DynamicJson {
	
	@Test(dataProvider = "BooksData")
	public void AddBook(String isbn, String aisle) {
		
		RestAssured.baseURI = "http://216.10.245.166";
		String response = given().header("Content-Type", "application/json")
		.body(payload.AddBookPay(isbn,aisle))
		.when().post("/Library/Addbook.php")
		.then().assertThat().statusCode(200).extract().response().asString();
		
		JsonPath js = ReUsableMethods.rawToJson(response);
		String id = js.get("ID");
		System.out.println(id);
	}
	
	@DataProvider(name="BooksData")
	public Object[][] getData() {
		
		return new Object[][] {{"hgvhj", "1003"},{"kjkjb", "1004"}, {"uyhb", "1005"}};
		
	}

}
