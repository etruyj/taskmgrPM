<?php

function connectToDatabase()
{
	$host = 'localhost';
	$database = 'db';
	$user = 'user';
	$password = 'pass';

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
