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
require 'headers/getFunctions.php';

$pdo = connectToDatabase();

var_dump($_POST);

switch($_POST['cmd'])
{
	case "listAccountDetails":
		$result = getAccountInfo($_POST['account_id'], $pdo);
		break;
	case "listAccounts":
		$result = getAllAccounts($pdo);
		break;
	case "login":
		$result = login($_POST['user'], $_POST['pass'], $pdo);
		break;
	case "testToken";
		$result = verifyToken($_POST['token'], $pdo);
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
	$testCredentials = verifyToken($_POST['token'], $pdo);
	if($testCredentials['code']<0)
	{
		$result = $testCredentials;
	}
}

// Return the results in JSON format.
header('Content-Type: application/json');
echo json_encode($result);
?>
