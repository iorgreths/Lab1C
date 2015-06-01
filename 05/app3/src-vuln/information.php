<html>

	<head>

		<title>A4 Test/information</title>

	</head>

	<body>

		<?php
			echo "<span>Wilkommen ".$_SESSION["uname"]."! Ihre Bankinformationen:<br/>";
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

			$sql = "SELECT Accountnumber, Balance FROM Information WHERE ID =".$_GET['id'];
			$result = $conn->query($sql);

			if ($result->num_rows > 0) {
			    // output data of each row
			    while($row = $result->fetch_assoc()) {
			        echo "Accountnummer: " . $row["Accountnumber"]. "<br/> Guthaben: " . $row["Balance"]. ".- EUR";
			    }
			} else {
			    echo "0 results";
			}
			$conn->close();
		?>

	</body>

</html>
