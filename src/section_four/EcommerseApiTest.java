package section_four;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.specification.RequestSpecification;
import pojo.LoginRequest;
import pojo.LoginResponse;
import pojo.OrderDetails;
import pojo.Orders;

import static io.restassured.RestAssured.*;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.testng.Assert;

public class EcommerseApiTest {

	public static void main(String[] args) {
		
		//Login
		RequestSpecification req = new RequestSpecBuilder().setBaseUri("https://rahulshettyacademy.com")
		.setContentType(ContentType.JSON).build();
		
		LoginRequest loginrequest = new LoginRequest();
		loginrequest.setUserEmail("manali.kulkarni@cogniwize.com");
		loginrequest.setUserPassword("Manali@123");
		
		RequestSpecification reqLogin = given()
			.log().all()
			.spec(req)
			.body(loginrequest);
		
		LoginResponse res = reqLogin.when()
			.post("/api/ecom/auth/login")
		.then()
			.log().all()
			.extract().as(LoginResponse.class);
		
		String token = res.getToken();
		String userID = res.getUserId();
		System.out.println(res.getToken());
		System.out.println(res.getUserId());
		System.out.println(res.getMessage());
		
		//add product
		RequestSpecification AddProductBaseReq = new RequestSpecBuilder().setBaseUri("https://rahulshettyacademy.com")
		.addHeader("Authorization", token).build();
		
		String addProductRes = given()
			.log().all()
			.spec(AddProductBaseReq)
			.param("productName", "anika")
			.param("productAddedBy", userID)
			.param("productCategory", "fashion")
			.param("productSubCategory", "shirts")
			.param("productPrice", "11500")
			.param("productDescription", "Addias Originals")
			.param("productFor", "women")
			.multiPart("productImage", new File("E:\\Documents\\my photo.jpg"))
		.when()
			.post("/api/ecom/product/add-product")
		.then()
			.log().all()
			.extract().response().asString();
		
		JsonPath js = new JsonPath(addProductRes);
		String productID = js.get("productId");
		
		//create order
		RequestSpecification createOrderBaseReq = new RequestSpecBuilder().setBaseUri("https://rahulshettyacademy.com")
		.addHeader("Authorization", token)
		.setContentType(ContentType.JSON).build();
		
		OrderDetails orderdetails = new OrderDetails();
		orderdetails.setCountry("India");
		orderdetails.setProductOrderedId(productID);
		
		List<OrderDetails> li = new ArrayList<OrderDetails>();
		li.add(orderdetails);
		
		Orders orders = new Orders();
		orders.setOrders(li);
		
		RequestSpecification createOrderReq = given()
			.log().all()
			.spec(createOrderBaseReq)
			.body(orders);
		
		String createOrderRes = createOrderReq.when()
			.post("/api/ecom/order/create-order")
		.then()
			.log().all()
			.extract().response().asString();
		System.out.println(createOrderRes);
		
		//Delete product
		RequestSpecification deleteProdBaseReq = new RequestSpecBuilder()
		.setBaseUri("https://rahulshettyacademy.com")
		.addHeader("Authorization", token)
		.setContentType(ContentType.JSON).build();
		
		RequestSpecification deleteProdReq = given().log().all().spec(deleteProdBaseReq)
		.pathParam("productId", productID);
		
		String deleteRes = deleteProdReq.when()
			.delete("/api/ecom/product/delete-product/{productId}")
		.then()
			.log().all()
			.extract().response().asString();
		
		JsonPath js1 = new JsonPath(deleteRes);
		Assert.assertEquals("Product Deleted Successfully", js1.getString("message"));
		
	}

}
