<html>
<body>
	User: 
		<?php
		//get form data.
		$loggingInUser = $_GET["username"];
		$submittedPass = $_GET["password"];
		//set sql db information
		$hostname = "localhost";
		$username = "root";
		$password = "";
		$db = "loginform";

		//Create connection
		$conn = new mysqli($hostname,$username,$password,$db);

		if($conn->connect_error){
			die("Connection failed: " . $conn->connect_error);
		}
		//select SQL information.
		$sql = "SELECT Username FROM users";
		$res = $conn->query($sql);

		if($res->num_rows > 0){

			while ($row = $res->fetch_assoc()) {
					
				$temp = $row['Username'];
				
				if($loggingInUser == $temp){
					echo $loggingInUser. " is a valid User.";
					header('Location: home.html');
					break;
				}else{
					echo "$loggingInUser is not a valid user.";
				}
			}
		}
		//get form inputted data


		
		 
	?>
</body>
</html>