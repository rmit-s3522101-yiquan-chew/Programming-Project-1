package model;
import java.util.regex.*;

public class Validate {
	FileTools fileTool = new FileTools();
	
	//Register validation
	//Age between 0 and 99
	public boolean validateAge(int age){
		if (age > 0 && age <= 99){
			return true;
		}
		return false;
	}
	//User_ID doesn't already exists
	public boolean valUserId(String ID){
		String filePath = fileTool.USER_DATA_FILE;
		
		if(fileTool.searchFile(ID, filePath) == null){
			return true;
		}
		return false;
	}
	//Must contain capital in front
	public boolean valName(String name){
		String pattern = "^[A-Z]{1}[a-zA-Z]+$";
		
		boolean isMatch = false;
		if(isMatch = Pattern.matches(pattern, name)){
			return true;
		}
		return false;
	}
	//At least 8 characters
	//At least 1 number
	//At least 1 UPPER
	public boolean valPassword(String password){
		String pattern = "^.*(?=.{8,})(?=..*[0-9])(?=.*[a-z])(?=.*[A-Z]).*$";
		
		boolean isMatch = Pattern.matches(pattern, password);
		return isMatch;
	}
	//Re-type password and confirm
	public boolean pwConfirm(String password, String confirm){
		if(password.equals(confirm)){
			return true;
		}
		return false;
	}
}
