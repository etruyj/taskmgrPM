<?php

//============================================================
// Services.php
//	This is the API handler for the taskmanager databse.
//
//	Functions
//	--listAccountDestails :: detail info for 1 account
//	--listAccounts
//	--login
//	--testToken :: test verifyToken functionality
//============================================================

require 'headers/credentials.php';
require 'headers/identityFunctions.php';

$pdo = connectToDatabase();

$options = json_decode($_POST['json']);

switch($_POST['cmd'])
{
	case "listAccountDetails":
		$result = getAccountInfo($options['account_id'], $pdo);
		break;
	case "listAccounts":
		$result = getAllAccounts($pdo);
		break;
	case "login":
		$result = login($options['user'], $options['pass'], $pdo);
		break;
	case "testToken";
		$result = verifyToken($option['token'], $pdo);
		break;
}

// Verify the user has a valid token at the end of the query.
// If the token is invalid, the return data from the functions
// will be overwritten. This saves adding this code at the 
// front end of every function or from including identity functions
// in each header file to allow for this verification to occur
// in function.
// Not necessary for login checks as no token should exist at this
// point.

if($_POST['cmd']!="login")
{
	$testCredentials = verifyToken($option['token'], $pdo);
	if($testCredentials['code']<0)
	{
		$result = $testCredentials;
	}
}

// Return the results in JSON format.
header('Content-Type: application/json');
echo json_encode($result);

?>
