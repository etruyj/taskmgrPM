<?php

//======================================================================
// getFunctions.php
// 	This functions are for querying the database and returning
// 	information to the user.
//
// 	Functions
// 	--getAccountContacts(account_id, pdo)
// 	--getAccountInfo(account_id, pdo)
// 	--getAccountProjects(account_id, pdo)
// 	--getAllAccounts(pdo)
// 	--getTracesByDatesAndUsers(token, start, end, pdo)
// 	--getTracesComplete(token, start, end, pdo)
// 	--getTracesIncomple(token, start, end, pdo)
//======================================================================

function getAccountContacts($account_id, $pdo)
{
	$sql = "SELECT contacts.contact_id, contacts.first_name, 
			contacts.last_name, contacts.position 
		FROM accounts 
		INNER JOIN contacts 
			ON accounts.account_id=contacts.account_id 
		WHERE accounts.account_id=:accID 
		ORDER BY contacts.last_name ASC;";

	$cmd = $pdo->prepare($sql);
	$cmd -> execute(array(":accID"=>$account_id));

	if($cmd->rowCount()>0)
	{
		$i = 0;
		foreach($cmd as $res)
		{
			$result[$i]['id'] 	= $res['contact_id'];
			$result[$i]['first'] 	= $res['first_name'];
			$result[$i]['last'] 	= $res['last_name'];
			$result[$i]['position'] = $res['position'];
			$i++;	
		}
	}
	else
	{
		$result = -1;
	}
	return $result;
	
}

function getAccountInfo($account_id, $pdo)
{
	// Retreive a list of contacts and projects in the account.

	$contacts = getAccountContacts($account_id, $pdo);
	$projects = getAccountProjects($account_id, $pdo);

	if($contacts!=-1)
	{
		$i = 0;
		foreach($contacts as $contact)
		{
			$result['contacts'][$i]['id'] = $contact['id'];
			$result['contacts'][$i]['first'] = $contact['first'];
			$result['contacts'][$i]['last'] = $contact['last'];
			$result['contacts'][$i]['position'] = $contact['position'];
			$i++;
		}
	}
	else
	{
		$result['contacts'][0]['id'] = "-1";
		$result['contacts'][0]['first'] = "none";
		$result['contacts'][0]['last'] = "none";
		$result['contacts'][0]['position'] = "none";
	}

	if($projects!=-1)
	{
		$i = 0;
		foreach($projects as $project)
		{
			$result['projects'][$i]['id'] = $project['id'];
			$result['projects'][$i]['name'] = $project['name'];
		}
	}
	else
	{
		$result['projects'][0]['id']=-1;
		$result['projects'][0]['name'] = "none";
	}


	return $result;
}

function getAccountProjects($account_id, $pdo)
{
	$sql = "SELECT projects.project_id, projects.project_name 
		FROM accounts 
		INNER JOIN projects 
			ON accounts.account_id=projects.account_id 
		WHERE accounts.account_id=:accID 
		ORDER BY projects.project_name DESC;";

	$cmd = $pdo->prepare($sql);
	$cmd -> execute(array(":accID"=>$account_id));

	if($cmd->rowCount()>0)
	{
		$i = 0;
		foreach($cmd as $res)
		{
			$result[$i]['id'] = $res['project_id'];
			$result[$i]['name'] = $res['project_name'];
		}
	}
	else
	{
		$result = -1;
	}

	return $result;
}

function getAllAccounts($pdo)
{
	// returns a list of account names and ids

	$sql = "SELECT account_id, name  
		FROM accounts  
		ORDER BY name ASC;";
	$cmd = $pdo->prepare($sql);
	$cmd -> execute();

	if($cmd->rowCount()>0)
	{
		$i = 0;
		foreach($cmd as $account)
		{
			$result[$i]["id"] = $account['account_id'];
			$result[$i]["name"] = $account['name'];
			$i++;
		}
	}
	else
	{
		$result = -1;
	}

	return $result;
}

?>
