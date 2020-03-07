<?php

function connectToDatabase()
{
	$host = 'localhost';
	$database = 'spectraPM';
	$user = 'spectraDaemon';
	$password = '3v4nD!ck1n50nJUn3GL45g0w';

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
