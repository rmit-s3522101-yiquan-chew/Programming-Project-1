import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.BeforeClass;
import org.junit.Test;

import model.Admin;
import model.FileTools;

public class AdminTest {
	
	static FileTools fileTool;
	static Admin admin;
	@BeforeClass
	public static void Before() {
		fileTool = new FileTools();
		admin = new Admin("admin", "Admin1234!", "John", "Nguyen", 20);
	}

	//@Test
	public void searchPlayertest() {
		ArrayList<String[]> match = admin.searchPlayer("bobby", fileTool.USER_DATA_FILE);
		
		for(int i = 0; i < match.size()-1; i++){
			assertTrue("error at index: " + i, match.get(i)[0].contains("bobby"));
		}
	}
	
	@Test
	public void searchTrAcc() {
		ArrayList<String[]> match = admin.searchTrAcc("bobby", fileTool.USER_DATA_FILE);
		
		for(int i = 0; i < match.size()-1; i++){
			assertTrue("error at index: " + i, match.get(i)[0].contains("bobby"));
		}
	}

}
