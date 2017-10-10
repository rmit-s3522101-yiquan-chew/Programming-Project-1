
import static org.junit.Assert.*;

import java.io.IOException;
import java.util.ArrayList;

import model.FileTools;
import model.Shares;
import model.User;

import org.json.JSONObject;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;


public class testFileTools {

	static FileTools fileTool;
	
	@BeforeClass
	public static void Before() {
		fileTool = new FileTools();
	}
	
	//SearchFile tests
	//@Test
	public void testSearchNotFound() {
		assertNull(fileTool.searchFile("fakeID", FileTools.USER_DATA_FILE));
	}
	
	//@Test
	public void testSearchFound() {
		assertNotNull(fileTool.searchFile("admin", FileTools.USER_DATA_FILE));
	}
	
	//@Test
	public void testSearchRighResultAdmin() {
		ArrayList<String[]> searchResult = fileTool.searchFile("admin", FileTools.USER_DATA_FILE);
		String[] admin = null;
		
		for (String[] res : searchResult)
		{
			if (res[0].equals("admin"))
			{
				admin = res;
			}
		}
		
		assertEquals("test search admin not match", "admin", admin[0]);
		assertEquals("test search admin not match", "#Admin123", admin[1]);
		assertEquals("test search admin not match", "Colonel", admin[2]);
		assertEquals("test search admin not match", "Sanders", admin[3]);
		assertEquals("test search admin not match", 50, Integer.parseInt(admin[4]));
	}
	
	//@Test
	public void testSearchRighResultPlayer() {
		ArrayList<String[]> searchResult =fileTool.searchFile("bobby123", FileTools.USER_DATA_FILE);
		String[] player = null;
		
		for (String[] res : searchResult)
		{
			if (res[0].equals("bobby123"))
			{
				player = res;
			}
		}
		
		assertEquals("test search Player not match", "bobby123", player[0]);
		assertEquals("test search Player not match", "Qwerty12!", player[1]);
		assertEquals("test search Player not match", "Bobby", player[2]);
		assertEquals("test search Player not match", "Smith", player[3]);
		assertEquals("test search Player not match", 20, Integer.parseInt(player[4]));
	}
	
	//LoadPlayer tests
	//@Test
	public void testLoadAdmin() {
		User user = fileTool.LoadUser("admin");
		assertEquals(user.getFName(), "Colonel");
	}
	
	//@Test
	public void testLoadPlayer() {
		User user = fileTool.LoadUser("bobby123");
		assertEquals(user.getFName(), "Dave");
	}
	
	//@Test
	public void testReadDailyJSON() {
		try {
			JSONObject json = new JSONObject(fileTool.fetchShareData("BHP", FileTools.DAILY_TIME_SERIES_STRING));
			assertEquals(((JSONObject)json.get("Meta Data")).get("2. Symbol"), "BHP.AX");
			assertTrue(json.has(FileTools.DAILY_TIME_SERIES_STRING));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		assertTrue(true);
	}
	
	//@Test
	public void testReadHourlyJSON() {
		try {
			JSONObject json = new JSONObject(fileTool.fetchShareData("BHP", FileTools.HOURLY_TIME_SERIES_STRING));
			assertEquals(((JSONObject)json.get("Meta Data")).get("2. Symbol"), "BHP.AX");
			assertTrue(json.has(FileTools.HOURLY_TIME_SERIES_STRING));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		assertTrue(true);
	}
	
	//this test takes long as heck due to function
	//@Test
	public void testFetchAll() {
		fileTool.fetchAllShareData();
		assertTrue(true);
	}
	
	//@Test
	public void testCSVtoJSON() {
		String json = fileTool.csvToJsonString(FileTools.USER_DATA_FILE);
		System.out.println(json);
		assertNotNull(json);
	}
	
	//@Test
	public void testLoadShare()
	{
		String ASXCode = "1AG";
		Shares share = null;
		try {
			share = FileTools.loadShare(ASXCode);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		assertTrue(share.getASX_code().equals(ASXCode));
	}
	
	@Test
	public void testUpdateAllStockVal()
	{
		try {
			fileTool.updateAllPlayerStockVal();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		assertTrue(true);
	}
	
	@AfterClass
	public static void After() {
		
	}

}
