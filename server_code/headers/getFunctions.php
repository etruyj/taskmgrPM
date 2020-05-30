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

function getAccountContacts($accountID, $pdo)
{
	$sql = "SELECT contacts.contact_id, contacts.first_name, 
			contacts.last_name, contacts.position 
		FROM accounts 
		INNER JOIN contacts 
			ON accounts.account_id=contacts.account_id 
		WHERE accounts.account_id=:account 
		ORDER BY contacts.last_name ASC;";

	$cmd = $pdo->prepare($sql);
	$cmd -> execute(array(":account"=>$accountID));

	if($cmd->rowCount()>0)
	{
		$result['msg'] = "Contacts successfully retrieved.";
		$result['code'] = 1;
		$i = 0;
		foreach($cmd as $res)
		{
			$result['contactList'][$i]['id'] 	= $res['contact_id'];
			$result['contactList'][$i]['first'] 	= $res['first_name'];
			$result['contactList'][$i]['last'] 	= $res['last_name'];
			$result['contactList'][$i]['position'] = $res['position'];
			$i++;	
		}
	}
	else
	{
		$result['msg'] = "Unable to retrieve contacts.";
		$result['code'] = -1;
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
		$result['msg'] = "Successfully retrieved projects.";
		$result['code'] = 1;
		$i = 0;
		foreach($cmd as $res)
		{
			$result['projectList'][$i]['id'] = $res['project_id'];
			$result['projectList'][$i]['name'] = $res['project_name'];
		}
	}
	else
	{
		$result['msg'] = "No projects found.";
		$result['code'] = -1;
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
		$result['msg'] = "Account list successfully retrieved.";
		$result['code'] = 1;
		$i = 0;
		foreach($cmd as $account)
		{
			$result['accountList'][$i]["id"] = $account['account_id'];
			$result['accountList'][$i]["account"] = $account['name'];
			$i++;
		}
	}
	else
	{
		$result['msg'] = "Unable to locate any accounts.";
		$result['code'] = -1;
	}

	return $result;
}

function getAllAccountsAllContactsAllProjects($pdo)
{
	$sql = 	"SELECT accounts.name, contacts.first_name, ";
	$sql .= "	contacts.last_name, projects.project_name ";
	$sql .= "FROM accounts ";
	$sql .= "INNER JOIN contacts ";
	$sql .= "	ON contacts.account_id=accounts.account_id ";
	$sql .= "INNER JOIN projects ";
	$sql .= "	ON projects.account_id=accounts.account_id ";
	$sql .= "ORDER BY accounts.name ASC;";

	$cmd = $pdo->prepare($sql);
       	$cmd->execute();

	if($cmd->rowCount()>0)
	{
		$name = "none";
		$i = -1;
		$c = 0;
		$p = 0;
		$result['msg'] = "Account query successful";
		$result['code'] = 1;
		foreach($cmd as $account)
		{
			if($account['account_name']!=$name)
			{
				$i++;
				$name = $account['account_name'];
			}

			$result['accounts'][$i]['name'] = $account['account_name'];
			$result['accounts'][$i]['contacts']['c'] = $account['account_name'];
		}
	}
	else
	{
		$result['msg'] = "Unable to find account information.";
		$result['code'] = -1;
	}	
}

function getTracesByDate($day, $pdo)
{
	$sql = "SELECT traces.trace_id, date, time, duration, type, ";
	$sql .=		"accounts.name, contacts.first_name, ";
	$sql .=		"contacts.last_name, projects.project_name, ";
	$sql .=		"subjects.subject, text.text ";
	$sql .=	"FROM traces ";
	$sql .=	"INNER JOIN accounts "; 
	$sql .=		"ON accounts.account_id=traces.account_id ";
	$sql .=	"INNER JOIN contacts "; 
	$sql .=		"ON contacts.contact_id=traces.contact_id ";
	$sql .=	"INNER JOIN projects ";
	$sql .=		"ON projects.project_id=traces.project_id ";
	$sql .=	"INNER JOIN users ";
	$sql .=		"ON users.user_id=traces.user_id ";
	$sql .=	"INNER JOIN subjects ";
	$sql .=		"ON subjects.subject_id=traces.subject_id ";
	$sql .=	"INNER JOIN text ";
	$sql .=		"ON text.text_id=traces.text_id ";
	$sql .=	"WHERE date=:day ";
	$sql .=	"ORDER BY time ASC;";

	$cmd = $pdo->prepare($sql);
	$cmd->execute(array(":day"=>$day));

	if($cmd->rowCount()>0)
	{
		$result['msg'] = "Traces found";
		$result['code'] = 1;
		$i = 0;

		foreach($cmd as $trace)
		{
			$result['traceList'][$i]['trace_id'] = $trace['trace_id'];	
			$result['traceList'][$i]['date'] = $trace['date'];
			$result['traceList'][$i]['time'] = $trace['time'];	
			$result['traceList'][$i]['duration'] = $trace['duration'];	
			$result['traceList'][$i]['type'] = $trace['type'];	
			$result['traceList'][$i]['account_name'] = $trace['name'];	
			$result['traceList'][$i]['contact_first'] = $trace['first_name'];	
			$result['traceList'][$i]['contact_last'] = $trace['last_name'];	
			$result['traceList'][$i]['project_name'] = $trace['project_name'];	
			$result['traceList'][$i]['subject'] = $trace['subject'];
			$result['traceList'][$i]['text'] = $trace['text'];
			$i++;	
		}
	}
	else
	{
		$result['msg'] = "There are no traces for this date.";
		$result['code'] = -1;
	}
	
	return $result;
}

function getTraceDetails($traceKey, $pdo)
{
	$sql =	"SELECT traces.trace_id, traces.date, traces.time, ";
	$sql .=		"traces.duration, traces.type, accounts.name, ";
	$sql .=		"contacts.first_name, contacts.last_name, ";		
	$sql .=		"projects.project_name, subjects.subject, ";
	$sql .=		"text.text, traces.completed ";
	$sql .=	"FROM traces ";
	$sql .=	"INNER JOIN accounts ";
	$sql .=		"ON accounts.account_id=traces.account_id ";
	$sql .=	"INNER JOIN contacts ";
	$sql .=		"ON contacts.contact_id=traces.contact_id ";
	$sql .=	"INNER JOIN projects ";
	$sql .=		"ON projects.project_id=traces.project_id ";
	$sql .=	"INNER JOIN subjects ";
	$sql .=		"ON subjects.subject_id=traces.subject_id ";
	$sql .= "INNER JOIN text ";
	$sql .=		"ON text.text_id=traces.text_id ";
	$sql .= "WHERE trace_id=:trace_key;";

	$cmd = $pdo->prepare($sql);
	$cmd->execute(array(":trace_key"=>$traceKey));

	if($cmd->rowCount()>0)
	{
		// Successfully query.
		$result['code'] = 1;
		$result['msg'] = "Trace info found.";

		// Load array
		foreach($cmd as $trace)
		{
			$result['trace_id'] = $trace['trace_id'];
			$result['date'] = $trace['date'];
			$result['time'] = $trace['time'];
			$result['duration'] = $trace['duration'];
			$result['type'] = $trace['type'];
			$result['account'] = $trace['name'];
			$result['first_name'] = $trace['first_name'];
			$result['last_name'] = $trace['last_name'];
			$result['project_name'] = $trace['project_name'];
			$result['subject'] = $trace['subject'];
			$result['text'] = $trace['text'];
			$result['completed'] = $trace['completed'];
		}
	}
	else
	{
		$result['msg'] = "Unable to locate trace information.";
		$result['code'] = -1;
	}

	return $result;
}
?>
