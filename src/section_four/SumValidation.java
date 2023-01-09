package section_four;

import org.testng.Assert;
import org.testng.annotations.Test;

import files.payload;
import io.restassured.path.json.JsonPath;

public class SumValidation {
	
	@Test
	public void verifyTotal() {
		
		int sum = 0;
		JsonPath js = new JsonPath(payload.GetCourses());
		int count = js.getInt("courses.size()");
		for (int i=0;i<count;i++) {
			int prices = js.getInt("courses["+i+"].price");
			int copies = js.getInt("courses["+i+"].copies");
			int total = prices * copies;
			System.out.println(total);
			sum = sum + total;
		}
		System.out.println(sum);
		int purchaseAmt = js.getInt("dashboard.purchaseAmount");
		Assert.assertEquals(sum, purchaseAmt);
	}

}
