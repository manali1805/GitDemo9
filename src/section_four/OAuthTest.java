package section_four;

import static io.restassured.RestAssured.*;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.Assert;

import io.restassured.parsing.Parser;
import io.restassured.path.json.JsonPath;
import pojo.GetCourse;
import pojo.WebAutomation;
import pojo.api;

public class OAuthTest {

	public static void main(String[] args) throws IOException, InterruptedException {

		//getCode
		/*System.setProperty("webdriver.chrome.driver", "E:\\Batches\\TS007\\chromedriver.exe");
		WebDriver driver = new ChromeDriver();
		driver.get("https://accounts.google.com/o/oauth2/v2/auth?scope=https://www.googleapis.com/auth/userinfo.email&auth_url=https://accounts.google.com/o/oauth2/v2/auth&client_id=692183103107-p0m7ent2hk7suguv4vq22hjcfhcr43pj.apps.googleusercontent.com&response_type=code&redirect_uri=https://rahulshettyacademy.com/getCourse.php");
		driver.findElement(By.xpath("//input[@type='email']")).sendKeys("manali.kulkarni@cogniwize.com");;
		Thread.sleep(5000);
		Properties p = new Properties();
		FileInputStream f = new FileInputStream("E:\\Rest Assured\\rahul shetty\\project\\DemoProject\\src\\files\\config.properties");
		p.load(f);
		driver.findElement(By.xpath("//*[@id=\"identifierNext\"]/div/button/span")).click();
		Thread.sleep(5000);
		driver.findElement(By.xpath("//input[@type='password']")).sendKeys(p.getProperty("password"));
		Thread.sleep(5000);
		driver.findElement(By.xpath("//*[@id=\"passwordNext\"]/div/button/span")).click();
		String currUrl = driver.getCurrentUrl();
		Thread.sleep(5000);*/
		String[] courseTitles = {"Selenium Webdriver Java", "Cypress", "Protractor"};
		
		String currUrl = "https://rahulshettyacademy.com/getCourse.php?code=4%2F0AWgavdefEOpcSP2yQjukiGFcH9NrfiVHfyphiNVD93r8l-D7qPUZJm1w2IJBKEyBuOgJhg&scope=email+https%3A%2F%2Fwww.googleapis.com%2Fauth%2Fuserinfo.email+openid&authuser=0&hd=cogniwize.com&prompt=none";
		String partialCode = currUrl.split("code=")[1];
		String code = partialCode.split("&scope")[0];
		System.out.println(code);

		//getAccessToken
		String accessTokenResponse = given().log().all()
				.urlEncodingEnabled(false)
				.queryParams("code", code)
				.queryParams("client_id", "692183103107-p0m7ent2hk7suguv4vq22hjcfhcr43pj.apps.googleusercontent.com")
				.queryParams("client_secret", "erZOWM9g3UtwNRj340YYaK_W")
				.queryParams("redirect_uri", "https://rahulshettyacademy.com/getCourse.php")
				.queryParams("grant_type", "authorization_code")
				.queryParams("state", "verifyfjdss")
				.queryParams("session_state", "ff4a89d1f7011eb34eef8cf02ce4353316d9744b..7eb8")
				.queryParams("scope","email+openid+https%3A%2F%2Fwww.googleapis.com%2Fauth%2Fuserinfo.email")
				.when()
				.post("https://www.googleapis.com/oauth2/v4/token").asString();

		System.out.println(accessTokenResponse);

		JsonPath js = new JsonPath(accessTokenResponse);
		String accessToken = js.get("access_token");
		System.out.println(accessToken);

		//getCourse
		/*String r2 = given().contentType("application/json").
		queryParam("access_token", accessToken).expect().defaultParser(Parser.JSON)
		.when().get("https://rahulshettyacademy.com/getCourse.php")
		.asString();

		System.out.println(r2);*/

		//getCourse
		GetCourse gc = given().contentType("application/json").
				queryParam("access_token", accessToken).expect().defaultParser(Parser.JSON)
				.when()
				.get("https://rahulshettyacademy.com/getCourse.php")
				.as(GetCourse.class);
		
		System.out.println(gc.getLinkedIn());
		System.out.println(gc.getInstructor());
		
		System.out.println(gc.getCourses().getApi().get(1).getCourseTitle());
		
		List<api> courses = gc.getCourses().getApi();
		for (int i=0;i<courses.size();i++) {
			if(courses.get(i).getCourseTitle().equalsIgnoreCase("SoapUI Webservices testing")) {
				System.out.println(courses.get(i).getPrice());
			}
		}
		
		//get all courses under webautomation
		ArrayList<String> a = new ArrayList<String>();
		List<WebAutomation> w = gc.getCourses().getWebAutomation();
		for(int j=0;j<w.size();j++) {
			a.add(w.get(j).getCourseTitle());
		}
		
		List<String> expectedCourses = Arrays.asList(courseTitles);
		Assert.assertTrue(a.equals(expectedCourses));
	}

}
