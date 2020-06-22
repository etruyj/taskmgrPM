<?php
//==============================================================================
// setFunctions.php
// 	These are the functions for saving data back into the the database
//==============================================================================

require_once 'identityFunctions.php';


function saveAcount($acc_id, $acc_name, $pdo)
{
	$result['code'] = 1;
	
	if($acc_id>0)
	{
		$sql =	"UPDATE accounts ";
		$sql .=	"SET name=:name ";
		$sql .= "WHERE account_id = :acc_id;";
		$cmd = $pdo->prepare($sql);
		$cmd->execute(array(
				":name"=>$acc_name,
				":acc_id"=>$acc_id));

		$result['msg'] = "Account $acc_name saved successfully.";
	}
	else
	{
		$sql = 	"INSERT INTO accounts ";
		$sql .=	"(name) VALUES (:name);";
		$cmd = $pdo->prepare($sql);
		$cmd->execute(array(":name"=>$acc_name));

		// Query accountts for new accountt ID.
		// This is a patch solution to prevent accidentally inserting
		// multiple copies of the account by clicking save again and
		// again.

		$sql = 	"SELECT account_id FROM accounts ";
		$sql .=	"WHERE name=:name ";
		$sql .= "ORDER BY account_id ASC;";

		$cmd = $pdo->prepare($sql);
		$cmd->execute(array(":name"=>$acc_name));

		if($cmd->rowCount()>0)
		{
			foreach($cmd as $account)
			{
				// Used order by ascending to guess that
				// the last contact entered that meets all
				// these values is the one we just saved in
				// the event there are duplicates.
				$acc_id = $account['account_id'];
			}
		}

		$result['msg'] = "New account $acc_name created.";
	}

	$result['id'] = $acc_id;

	return $result;
}

function saveContact($contact_id, $acc_id, $first_name, $last_name, $position, $email, $phone, $includeInSearches, $pdo)
{
	$result['code'] = 1;

	if($contact_id>0)
	{
		$sql = 	"UPDATE contacts ";
		$sql .=	"SET first_name=:first, last_name=:last, ";
		$sql .=		"position=:pos, phone=:phone, email=:email, ";
		$sql .=		"include_in_searches=:inc ";
		$sql .=	"WHERE contact_id=:cont_id;";
		
		$cmd = $pdo->prepare($sql);
		$cmd->execute(array(
				":first"=>$first_name,
				":last"=>$last_name,
				":pos"=>$position,
				":phone"=>$phone,
				":email"=>$email,
				":inc"=>$includeInSearches,
				":cont_id"=>$contact_id));

		$result['msg'] = "Saved $last_name, $first_name successfully.";
	}
	else
	{
		// Insert new contact into contacts.
		$sql = 	"INSERT INTO contacts ";
		$sql .= "(first_name, last_name, account_id, position, ";
		$sql .= 	"phone, email, include_in_searches) ";
		$sql .= "VALUES ";
		$sql .= "(:first, :last, :acc, :pos, :phone, :email, :inc);";

		$cmd = $pdo->prepare($sql);
		$cmd->execute(array(
				":first"=>$first_name,
				":last"=>$last_name,
				":acc"=>$acc_id,
				":pos"=>$position,
				":phone"=>$phone,
				":email"=>$email,
				":inc"=>$includeInSearches));

		// Query contacts for new contact ID.
		// This is a patch solution to prevent accidentally inserting
		// multiple copies of the contact by clicking save again and
		// again.

		$sql = 	"SELECT contact_id FROM contacts ";
		$sql .= "WHERE first_name=:first AND last_name=:last AND ";
		$sql .=		"account_id=:acc AND position=:pos AND ";
		$sql .=		"phone=:phone AND ";
		$sql .=		"email=:email AND include_in_searches=:inc ";
		$sql .=	"ORDER BY contact_id ASC;";

		$cmd = $pdo->prepare($sql);
		$cmd->execute(array(
				":first"=>$first_name,
				":last"=>$last_name,
				":acc"=>$acc_id,
				":pos"=>$position,
				":phone"=>$phone,
				":email"=>$email,
				":inc"=>$includeInSearches));

		if($cmd->rowCount()>0)
		{
			foreach($cmd as $contact)
			{
				// Used order by ascending to guess that
				// the last contact entered that meets all
				// these values is the one we just saved in
				// the event there are duplicates.
				$contact_id = $contact['contact_id'];
			}
		}
		// No else statement is needed as the old contact id will
		// be used.
		
		$result['msg'] = "($acc_id) New contact $last_name, $first_name created.";
	}
	
	$result['id'] = $contact_id;

	return $result;
}

function saveProject($proj_id, $acc_id, $proj_name, $pdo)
{
	$result['code'] = 1;

	if($proj_id>0)
	{
		$sql =	"UPDATE projects ";
		$sql .=	"SET project_name=:proj, account_id=:acc ";
		$sql .= "WHERE project_id=:proj_id;";
		
		$cmd = $pdo->prepare($sql);
		$cmd->execute(array(
				":proj"=>$proj_name,
				":acc"=>$acc_id,
				":proj_id"=>$proj_id));

		$result['msg'] = "Project $proj_name was saved successfully.";
	}
	else
	{
		$sql =	"INSERT INTO projects ";
		$sql .= "(project_name, account_id) ";
		$sql .=	"VALUES ";
		$sql .= "(:proj, :acc);";

		$cmd = $pdo->prepare($sql);
		$cmd->execute(array(
				":proj"=>$proj_name,
				":acc"=>$acc_id));

		// Query contacts for new contact ID.
		// This is a patch solution to prevent accidentally inserting
		// multiple copies of the contact by clicking save again and
		// again.

		$sql = 	"SELECT project_id FROM projects ";
		$sql .= "WHERE project_name=:proj AND account_id=:acc ";
		$sql .= "ORDER BY project_id ASC;";

		$cmd = $pdo->prepare($sql);
		$cmd->execute(array(
				":proj"=>$proj_name,
				":acc"=>$acc_id));

		if($cmd->rowCount()>0)
		{
			foreach($cmd as $project)
			{
				// Used order by ascending to guess that
				// the last contact entered that meets all
				// these values is the one we just saved in
				// the event there are duplicates.
				$proj_id = $project['project_id'];
			}
		}

		$result['msg'] = "Project $proj_name created.";
	}

	$result['id'] = $proj_id;

	return $result;
}

function saveTraceDetails($trace_id, $account_id, $contact_id, $project_id, $date, $time, $duration, $type, $subject, $text, $combool, $pdo)
{
	if($combool==1)
	{
		$completed = 1;
	}
	else
	{
		$completed = 0;
	}


	if($trace_id>0)
	{
		$result = saveExistingTrace($trace_id, $account_id, $contact_id, $project_id, $date, $time, $duration, $type, $subject, $text, $completed, $pdo);
 	}
	else
	{
		// If trace_id < 0 the trace is a new trace.
		$result = saveNewTrace($account_id, $contact_id, $project_id, $date, $time, $duration, $type, $subject, $text, $completed, $pdo);
	}

	return $result;
}

function saveExistingTrace($trace_id, $account_id, $contact_id, $project_id, $date, $time, $duration, $type, $subject, $text, $completed, $pdo)
{
	// If trace_id > 0 the trace is an existing trace.
	// Information needs to be updated and the keys for the
	// existing subject and text fields need to be located.

	$sql 	= 	"SELECT subject_id, text_id ";
	$sql 	.= 	"FROM traces ";
	$sql	.=	"WHERE trace_id=:trc;";

	$cmd = $pdo->prepare($sql);
	$cmd->execute(array(":trc"=>$trace_id));

	if($cmd->rowCount()>0)
	{
		// Query Successful.
		foreach($cmd as $trace)
		{
			// Update the subject, text, and then the
			// trace tables.
			$subd 	= 	"UPDATE subjects ";
			$subd	.=	"SET subject=:subj ";
			$subd	.=	"WHERE subject_id=:subjID;";

			$txm = $pdo->prepare($subd);
			$txm->execute(array(
					":subj"=>$subject,
					":subjID"=>$trace['subject_id']));

			$texd 	=	"UPDATE text ";
			$texd	.=	"SET text=:txt ";
			$texd	.=	"WHERE text_id=:txtID;";

			$txm = $pdo->prepare($texd);
			$txm->execute(array(
					":txt"=>$text,
					":txtID"=>$trace['text_id']));

			$trcd	=	"UPDATE traces ";
			$trcd	.=	"SET date=:date, time=:time, duration=:dur, ";
			$trcd	.=		"type=:type, contact_id=:cont, ";
			$trcd	.=		"account_id=:acc, project_id=:proj, ";
			$trcd	.=		"completed=:complete ";
			$trcd	.=	"WHERE trace_id=:trcid;";

			$txm = $pdo->prepare($trcd);
			$txm->execute(array(
					":date"=>$date,
					":time"=>$time,
					":dur"=>$duration,
					":type"=>$type,
					":cont"=>$contact_id,
					":acc"=>$account_id,
					":proj"=>$project_id,
					":complete"=>$completed,
					":trcid"=>$trace_id));

			$result['msg'] = "Trace updated successfully. $combool:$completed";
			$result['code'] = 1;
		}
	}
	else
	{
		// Unable to locate trace
		$result['msg'] = "Unable to locate existing trace.";
		$result['code'] = -1;
	}

	return $result;
}

function saveNewTrace($account_id, $contact_id, $project_id, $date, $time, $duration, $type, $subject, $text, $completed, $pdo)
{
	// Subject and text fields need to be created and identified.
		
	// Create entries for subject and text tables and query for the
	// key. As likelihood of human readable content having multiple 
	// identical increases as these tables increase. I'm using the generateToken()
	// field from the identity functions to create a pseudo-random
	// string to decrease the likelihood of this problems.
		
	$uniqueString = generateToken();
	$subjID = 0; // Using this value to test for success in id generation query.
	$textID = 0; // Using this value to test for success in id generation query.

	// Create and retrieve the subject index.
	$sql = "INSERT INTO subjects (subject) VALUES (:subj);";
	$cmd = $pdo->prepare($sql);
	$cmd->execute(array(":subj"=>$uniqueString));

	$sql = "SELECT subject_id FROM subjects WHERE subject=:subj;";
	$cmd = $pdo->prepare($sql);
	$cmd->execute(array(":subj"=>$uniqueString));

	if($cmd->rowCount()>0)
	{
		foreach($cmd as $subj)
		{
			$subjID = $subj['subject_id'];
		}
	}

	// Create and retreive the text index.
	$sql = "INSERT INTO text (text) VALUES (:txt);";
       	$cmd = $pdo->prepare($sql);
	$cmd->execute(array(":txt"=>$uniqueString));

	$sql = "SELECT text_id FROM text WHERE text=:txt;";
	$cmd = $pdo->prepare($sql);
	$cmd->execute(array(":txt"=>$uniqueString));

	if($cmd->rowCount()>0)
	{
		foreach($cmd as $txt)
		{
			$txtID = $txt['text_id'];
		}
	}

	// Test to see if the above two queries were successful.
	// Without text and subject indext fields, there's no
	// reason to continue saving this trace. It failed.
	if(($subjID>0) && ($txtID>0))
	{
		// Upload subject and text to those tables.
		$sql 	= 	"UPDATE subjects ";
		$sql 	.= 	"SET subject=:subj ";
		$sql	.=	"WHERE subject_id=:subjID;";

		$cmd = $pdo->prepare($sql);
		$cmd->execute(array(
				":subj"=>$subject,
				":subjID"=>$subjID));

		$sql 	=	"UPDATE text ";
		$sql	.=	"SET text=:txt ";
		$sql	.=	"WHERE text_id=:txtID;";

		$cmd = $pdo->prepare($sql);
		$cmd->execute(array(
				":txt"=>$text,
				":txtID"=>$txtID));

		// Create the trace in the index field.
		$sql 	=	"INSERT INTO traces ";
		$sql	.=	"(account_id, contact_id, project_id, date, time, type, ";
		$sql	.=		"duration, subject_id, text_id, completed) ";
		$sql	.=	"VALUES ";
		$sql	.=	"(:acc, :cont, :proj, :date, :time, :type, :dur, :subj, :txt, :comp)";

		$cmd = $pdo->prepare($sql);
		$cmd->execute(array(
				":acc"=>$account_id,
				":cont"=>$contact_id,
				":proj"=>$project_id,
				":date"=>$date,
				":time"=>$time,
				":type"=>$type,
				":dur"=>$duration,
				":subj"=>$subjID,
				":txt"=>$txtID,
				":comp"=>$completed));
		
		$result['msg'] = "Successfully saved new trace.";
		$result['code'] = 1;
	}
	else
	{
		$result['msg'] = "Unable to save new trace. Could not generate index keys. ($subjID:$txtID)";
		$result['code'] = -2;
	}
	
	return $result;
}
?>
