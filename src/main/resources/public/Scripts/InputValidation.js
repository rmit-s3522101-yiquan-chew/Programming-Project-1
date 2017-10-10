function regValidate() {
	var user_name = document.forms["regForm"]["username"];
	var first_name = document.forms["regForm"]["firstname"];
	var last_name = document.forms["regForm"]["lastname"];
	var age = document.forms["regForm"]["age"];
	var password = document.forms["regForm"]["password"];
	var confirm_password = document.forms["regForm"]["confirmpassword"];

	var user_name_error = document.getElementById("user_name_error");
	var first_name_error = document.getElementById("first_name_error");
	var last_name_error = document.getElementById("last_name_error");
	var age_error = document.getElementById("age_error");
	var password_error = document.getElementById("password_error");
	
	// Checking if nothing has been entered
	function isEmpty(value) {
		if (value === "") {
			return true;
		}
		return false;
	}

//	function userExists(username) {
//		var userObj = csvToJSON("/UserData.csv");
//		//console.log(userObj);
//		return true;
//	}
	
	// Validates user name
	function valUserName(username) {
		var isMatch = username.match(/^[a-zA-Z0-9]+$/g);
		return isMatch;
	}
	
	// Validates name
	function valName(name) {
		var isMatch = name.match(/^[A-Z][a-z]+$/g);
		return isMatch;
	}
	
	// Validates age
	function valAge(age) {
		if (age > 0 && age <= 99) {
			return true;
		}
	}

	// Validates password
	function valPassword(password) {
		var isMatch = password.match(/^.*(?=.{8,})(?=..*[0-9])(?=.*[a-z])(?=.*[A-Z]).*$/g);
		return isMatch;
	}
	
	// Returns text box to normal upon correct input
	function usernameVerify() {
		if (!isEmpty(user_name.value)) {
			user_name.style.border = "thin solid black";
			return true;
		}
	}

	function firstNameVerify() {
		if (!isEmpty(first_name.value)) {
			first_name.style.border = "thin solid black";
			return true;
		}
	}

	function lastNameVerify() {
		if (!isEmpty(last_name.value)) {
			last_name.style.border = "thin solid black";
			return true;
		}
	}

	function ageVerify() {
		if (!isEmpty(age.value)) {
			age.style.border = "thin solid black";
			return true;
		}
	}

	function passwordVerify() {
		if (!isEmpty(password.value)) {
			password.style.border = "thin solid black";
			return true;
		}
	}
	
	// Event listeners for text boxes
	user_name.addEventListener("blur", usernameVerify, true);
	first_name.addEventListener("blur", firstNameVerify, true);
	last_name.addEventListener("blur", lastNameVerify, true);
	age.addEventListener("blur", ageVerify, true);
	password.addEventListener("blur", passwordVerify, true);
		
	// user name validation
	if (!valUserName(user_name.value)) {
		user_name.style.border = "1px solid red";
		user_name_error.style.color = "red";
		user_name_error.textContent = "Valid username is required";
		user_name.focus();
		return false;
	}
	user_name_error.innerHTML = "";
	// user name existence check
//	if(userExists(user_name.value) {
//		user_name.style.border = "1px solid red";
//		user_name_error.textContent = "User name already is use";
//		user_name.focus();
//		return false;
//	}
//	user_name_error.innerHTML = "";
	// first name validation
	if (!valName(first_name.value)) {
		first_name.style.border = "1px solid red";
		first_name_error.style.color = "red";
		first_name_error.textContent = "Must have a capital letter at the start and only contains letters";
		first_name.focus();
		return false;
	}
	first_name_error.innerHTML = "";
	// last name validation
	if (!valName(last_name.value)) {
		last_name.style.border = "1px solid red";
		last_name_error.style.color = "red";
		last_name_error.textContent = "Must have a capital letter at the start and only contains letters";
		last_name.focus();
		return false;
	}
	last_name_error.innerHTML = "";
	// age validation
	if (!valAge(age.value)) {
		age.style.border = "1px solid red";
		age_error.style.color = "red";
		age_error.textContent = "Valid age is required";
		age.focus();
		return false;
	}
	age_error.innerHTML = "";
	// password validation
	if (!valPassword(password.value)) {
		password.style.border = "1px solid red";
		password_error.style.color = "red";
		password_error.textContent = "Password must have at least: 8 characters, a number and special character";
		password.focus();
		return false;
	}
	password_error.innerHTML = "";
	// check if passwords match
	if (password.value !== confirm_password.value) {
		password.style.border = "1px solid red";
		password_error.style.color = "red";
		confirm_password.style.border = "1px solid red";
		password_error.innerHTML = "Passwords do not match";
		return false;
	}
	password_error.innerHTML = "";
	return true;
}