import static org.junit.Assert.*;

import java.io.IOException;

import org.junit.BeforeClass;
import org.junit.Test;

import model.FileTools;
import model.Menu;

public class MenuTest {
	
	public static Menu menu;
	public static FileTools fileTool;
	
	@BeforeClass
	public static void init(){
		menu = new Menu();
		fileTool = new FileTools();
	}

	@Test
	public void registerTest() throws IOException {
		int numMembers = fileTool.readCSV(fileTool.USER_DATA_FILE).size();
		
		menu.register("johnny1014", "John", "Nguyen", 20, "Zxcvbn56%", "Zxcvbn56%");
		int newNum = fileTool.readCSV(fileTool.USER_DATA_FILE).size(); 
		System.out.println("Members before Register: " + numMembers);
		System.out.println("Members after Register: " + newNum);
		assertTrue(newNum == numMembers + 1);
	}

}
