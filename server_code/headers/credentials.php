<?php

function connectToDatabase()
{
	$host = 'localhost';
	$db = 'databaseName';
	$user = 'user';
	$pass = 'passwd';

	try
	{
		$pdo = new PDO("mysql:host=$host;dbname=$db", $user, $pass);

	}
	catch(PDOException $e)
	{
		$result['msg'] = "Error connecting to database.";
		$result['code'] = -1;
		echo $result;
		exit();
	}

	return $pdo;
}

?>
