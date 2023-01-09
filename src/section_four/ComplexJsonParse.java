package section_four;

import files.payload;
import io.restassured.path.json.JsonPath;

public class ComplexJsonParse {

	public static void main(String[] args) {
		
		JsonPath js = new JsonPath(payload.GetCourses());
		
		//Print No of courses returned by API
		int courseCount = js.getInt("courses.size()");
		System.out.println(courseCount);
		
		//Print Purchase Amount
		int purchaseAmt = js.getInt("dashboard.purchaseAmount");
		System.out.println(purchaseAmt);
		
		//Print Title of the first course
		String CourseFirstTitle = js.getString("courses[0].title");
		System.out.println(CourseFirstTitle);
		
		//Print All course titles and their respective Prices
		for (int i=0;i<courseCount;i++) {
			String couseTitles = js.get("courses["+i+"].title");
			System.out.println(js.get("courses["+i+"].price").toString());
			System.out.println(couseTitles);
		}
		
		System.out.println("Print no of copies sold by RPA Course");
		for (int i=0;i<courseCount;i++) {
			String couseTitles = js.get("courses["+i+"].title");
			if(couseTitles.equalsIgnoreCase("RPA")) {
				System.out.println(js.get("courses["+i+"].copies").toString());
				break;
			}
		}
	}

}
