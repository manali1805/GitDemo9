package files;

import io.restassured.path.json.JsonPath;

public class ReUsableMethods {
	
	public static JsonPath rawToJson(String response) {
		JsonPath js1 = new JsonPath(response);
		
		System.out.println("jira1");
		
		System.out.println("jira2");
		System.out.println("jira3");
		
		System.out.println("jira4 arch");
		System.out.println("jira5 arch");
		System.out.println("jira6 arch");
		
		System.out.println("jira7 arch");
		System.out.println("jira8 arch");
		System.out.println("jira9 arch");
		
		return js1;
	}

}
