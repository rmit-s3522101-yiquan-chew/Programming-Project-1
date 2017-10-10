package model;

import java.io.IOException;

public class Menu {
	FileTools fileTool = new FileTools();
	Validate valTool = new Validate();

	public Boolean login(String username, String password)
	{
		User user = fileTool.LoadUser(username);
		
		//if input user name does not exist
		if (user == null)
		{
			return false;
		}
		
		//if input user name found, check password
		if (user.getPassword().equals(password))
		{
			return true;
		}
		else
		{
			return false;
		}
	}
	
	public boolean register(String user_ID, String user_fname, 
			                String user_lname, int age,
			                String password, String pwConfirm) throws IOException{
		
//		//User input validation --> See Validae Class
//		//Early Exit
//		if(!valTool.valUserId(user_ID) || user_ID.equals(null)){
//			//Error Message: "user_ID already exists"
//			//Error Message: "Field left empty"
//			return false;
//		}
//		
//		if(!valTool.validateAge(age)){
//			//Error Message: "Age must be between 0 and 99"
//			return false;
//		}
//		
//		if(!valTool.valName(user_fname) || user_fname.equals(null) 
//		&& !valTool.valName(user_lname) || user_lname.equals(null)){
//			//Error Message: "Name must begin with an upper-case"
//			//Error Message: "Field left empty"
//			return false;
//		}
//		
//		if(!valTool.valPassword(password)){
//			//Error Message: At least 8 character long
//			              // At least 1 Upper-case
//		                  // At least 1 digit
//			return false;
//		}
//		
//		if(!valTool.pwConfirm(password, pwConfirm)){
//			return false;
//		}
		
		//create a new user and writes to files
		User regUser = new Player(user_ID, password, user_fname, user_lname, age);
		fileTool.playerToFile(regUser);
		
		return true; 
	}
	
}
