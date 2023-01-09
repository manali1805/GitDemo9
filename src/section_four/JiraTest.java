package section_four;

import io.restassured.RestAssured;
import io.restassured.filter.session.SessionFilter;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;

import static io.restassured.RestAssured.*;

import java.io.File;

import org.testng.Assert;

public class JiraTest {

	public static void main(String[] args) {
		
		RestAssured.baseURI = "http://localhost:8080";
		
		SessionFilter session = new SessionFilter();
		
		//Login
		String response = given().relaxedHTTPSValidation().log().all().header("Content-Type", "application/json")
		.body("{\r\n"
				+ "    \"username\": \"manali\",\r\n"
				+ "    \"password\": \"manali\"\r\n"
				+ "}").filter(session)
		.when().post("/rest/auth/1/session")
		.then().log().all().extract().response().asString();
		
		//Add comment
		String expmsg = "Hi, How are you?";
		String addCommentResponse = given().log().all().pathParam("key", "10005").header("Content-Type", "application/json")
		.body("{\r\n"
				+ "    \"body\" : \""+expmsg+"\",\r\n"
				+ "    \"visibility\" : {\r\n"
				+ "        \"type\": \"role\",\r\n"
				+ "        \"value\" : \"Administrators\"\r\n"
				+ "    }\r\n"
				+ "}").filter(session)
		.when().post("/rest/api/2/issue/{key}/comment")
		.then().log().all().assertThat().statusCode(201).extract().response().asString();
		JsonPath js = new JsonPath(addCommentResponse);
		String commentId = js.get("id");
		
		//Add attachment
		given().log().all().filter(session).pathParam("key", "10005").header("X-Atlassian-Token", "nocheck")
		.header("Content-Type", "multipart/form-data")
		.multiPart("file", new File("jira.txt"))
		.when().post("/rest/api/2/issue/{key}/attachments")
		.then().log().all().assertThat().statusCode(200);
		
		//Get Issue
		String issueDetails = given().filter(session).
				pathParam("key", "10005").log().all()
				.queryParam("fields", "comment")
		.when().get("/rest/api/2/issue/{key}")
		.then().log().all().extract().response().asString();
		System.out.println(issueDetails);
		
		//verify the comments
		JsonPath js1 = new JsonPath(issueDetails);
		int CommentCount = js1.getInt("fields.comment.comments.size()");
		for (int i=0;i<CommentCount;i++) {
			String commentIdIssue = js1.get("fields.comment.comments["+i+"].id");
			if(commentIdIssue.equalsIgnoreCase(commentId)) {
				String msg = js1.get("fields.comment.comments["+i+"].body").toString();
				System.out.println(msg);
				Assert.assertEquals(expmsg, msg);
			}
		}
		

	}

}
