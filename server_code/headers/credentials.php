<?php

function connectToDatabase()
{
	$host = 'localhost';
	$db = 'spectraPM';
	$user = 'spectraDaemon';
	$pass = '3v4nD!ck1n50nJUn3GL45g0w';

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
