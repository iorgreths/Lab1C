<?php
	session_start();
	$_SESSION['uname'] = $_POST["username"];
	$servername = "localhost";
	$username = "user2730118";
	$password = "lab1lab1";
	$dbname = "db2730118-main";

	// Create connection
	$conn = new mysqli($servername, $username, $password, $dbname);

	// Check connection
	if ($conn->connect_error) {
    		die("Connection failed: " . $conn->connect_error);
	} 
	$name = $_SESSION['uname'];

	$sql = "SELECT ID FROM Users WHERE Username='".$name."'";
	$result = $conn->query($sql);
	$row = $result->fetch_assoc();
	$redirect="Location: information.php?id=".$row["ID"];
	header($redirect);
?>
