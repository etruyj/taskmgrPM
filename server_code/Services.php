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

// Verify the user has a valid token at the end of the query.
// Not necessary for login checks as no token should exist at this
// point.

$data = json_decode(file_get_contents('php://input'), true);

$testCredentials = verifyToken($data['token'], $pdo);
if($testCredentials['code']<0 && $data['cmd']!='login')
{
	$result = $testCredentials;
}
else
{
	switch($data['cmd'])
	{
		case "listAccountContacts":
			$result = getAccountContacts($data['account_id'], $pdo);
			break;
		case "listAccountDetails":
			$result = getAccountInfo($data['account_id'], $pdo);
			break;
		case "listAccountProjects":
			$result = getAccountProjects($data['account_id'], $pdo);
			break;
		case "listAccounts":
			$result = getAllAccounts($pdo);
			break;
		case "listTraces":
			$result = getTracesByDate($data['day'], $pdo);
			break;
		case "listTraceDetails":
			$result = getTraceDetails($data['trace_id'], $pdo);
			break;
		case "login":
			$result = login($data['user'], $data['pass'], $pdo);
			break;
		case "testToken":
			$result = verifyToken($data['token'], $pdo);
			break;
		default:
			$result['msg'] = "Invalid selection";
			$result['code'] = -99;

	}
}

// Return the results in JSON format.
header('Content-Type: application/json');
echo json_encode($result);
?>
