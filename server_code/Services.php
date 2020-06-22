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
require 'headers/setFunctions.php';

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
		case "listTraceContacts":
			$result = getAccountContactsForTrace($data['account_id'], $pdo);
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
		case "saveAccount":
			$result = saveAccount($data['account_id'], $data['name'], $pdo);
			break;
		case "saveContact":
			$result = saveContact($data['contact_id'], $data['account_id'], $data['firstName'], $data['lastName'], $data['position'], $data['email'], $data['phone'], $data['toInclude'], $pdo);
			break;
		case "saveProject":
			$result = saveProject($data['project_id'], $data['account_id'], $data['name'], $pdo);
			break;
		case "saveTrace":
			$result = saveTraceDetails($data['trace_id'], $data['account_id'], $data['contact_id'], $data['project_id'], $data['date'], $data['time'], $data['duration'], $data['type'], $data['subject'], $data['text'], $data['completed'], $pdo);
			break;
		case "searchAccounts":
			$result = getAllAccountsFromSearch($data['query'], $pdo);
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
