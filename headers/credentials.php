<?php

function connectToDatabase()
{
	$host = 'localhost';
	$database = /* database name */;
	$user = /* username */;
	$password = /* password */;

	try
	{
		$pdo = new PDO("mysql:host=$host;dbname=$database", $user, $password);
	}
	catch(PDOException $e)
	{
		echo 'Error connecting to database. ';
		exit();
	}

	return $pdo;
}


?>
