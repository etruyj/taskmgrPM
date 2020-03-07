<?php

// =============================================================
// identityFunctions.php
//	All the functions related to identity management.
//
//	Functions
//		--checkPassword(password, hash)
//		--generateToken()
//		--login($userHash, $pass, $pdo)
//		--logout($token, $pdo)
//		--storeToken($user_id, $token, $pdo)
//		--verifyToken($token, $pdo);
// =============================================================

function checkPassword($pass, $hash)
{
	return password_verify($pass, $hash);
}

function generateToken()
{
	$token = '';

	for($i=0; $i<20; $i++)
	{
		$val = rand(0, 75);

		switch ($val)
		{
			case 0:
				$token.="0";
				break;
			case 1:
				$token.="0";
				break;
			case 2:
				$token.="0";
				break;
			case 3:
				$token.="0";
				break;
			case 4:
				$token.="0";
				break;
			case 5:
				$token.="0";
				break;
			case 6:
				$token.="0";
				break;
			case 7:
				$token.="0";
				break;
			case 8:
				$token.="0";
				break;
			case 9:
				$token.="0";
				break;
			case 10:
				$token.="A";
				break;
			case 11:
				$token.="B";
				break;
			case 12:
				$token.="C";
				break;
			case 13:
				$token.="D";
				break;
			case 14:
				$token.="E";
				break;
			case 15:
				$token.="F";
				break;
			case 16:
				$token.="G";
				break;
			case 17:
				$token.="H";
				break;
			case 18:
				$token.="I";
				break;
			case 19:
				$token.="J";
				break;
			case 20:
				$token.="K";
				break;
			case 21:
				$token.="L";
				break;
			case 22:
				$token.="M";
				break;
			case 23:
				$token.="N";
				break;
			case 24:
				$token.="O";
				break;
			case 25:
				$token.="P";
				break;
			case 26:
				$token.="Q";
				break;
			case 27:
				$token.="R";
				break;
			case 28:
				$token.="S";
				break;
			case 29:
				$token.="T";
				break;
			case 30:
				$token.="U";
				break;
			case 31:
				$token.="V";
				break;
			case 32:
				$token.="W";
				break;
			case 33:
				$token.="X";
				break;
			case 34:
				$token.="Y";
				break;
			case 35:
				$token.="Z";
				break;
			case 36:
				$token.="a";
				break;
			case 37:
				$token.="b";
				break;
			case 38:
				$token.="c";
				break;
			case 39:
				$token.="d";
				break;
			case 40:
				$token.="e";
				break;
			case 41:
				$token.="f";
				break;
			case 42:
				$token.="g";
				break;
			case 43:
				$token.="h";
				break;
			case 44:
				$token.="i";
				break;
			case 45:
				$token.="j";
				break;
			case 46:
				$token.="k";
				break;
			case 47:
				$token.="l";
				break;
			case 48:
				$token.="n";
				break;
			case 49:
				$token.="o";
				break;
			case 50:
				$token.="p";
				break;
			case 51:
				$token.="q";
				break;
			case 52:
				$token.="r";
				break;
			case 53:
				$token.="s";
				break;
			case 54:
				$token.="t";
				break;
			case 55:
				$token.="u";
				break;
			case 56:
				$token.="v";
				break;
			case 57:
				$token.="w";
				break;
			case 58:
				$token.="x";
				break;
			case 59:
				$token.="y";
				break;
			case 60:
				$token.="z";
				break;
			case 61:
				$token.="!";
				break;
			case 62:
				$token.="@";
				break;
			case 63:
				$token.="#";
				break;
			case 64:
				$token.="$";
				break;
			case 65:
				$token.="%";
				break;
			case 66:
				$token.="^";
				break;
			case 67:
				$token.="&";
				break;
			case 68:
				$token.="*";
				break;
			case 69:
				$token.="(";
				break;
			case 70:
				$token.=")";
				break;
			case 71:
				$token.="-";
				break;
			case 72:
				$token.="+";
				break;
			case 73:
				$token.="=";
				break;
			case 74:
				$token.="_";
				break;
			case 75:
				$token.="?";
				break;
		}		
	}

	return $token;
}

function login($user, $pass, $pdo)
{
	//====================================================
	// login
	// 	Verifies the username hash and password hash
	// 	against the values in the user table.
	// 	If valid, a token is generated and returned to
	// 	the user. The token is saved in the table along
	// 	with a time stamp.
	//====================================================

	$sql = "SELECT contacts.first_name, contacts.last_name,
			users.password, users.user_id  
		FROM users 
		INNER JOIN contacts 
			ON users.contact_id = contacts.contact_id 
		WHERE username=:user;";

	$cmd = $pdo->prepare($sql);
	$cmd -> execute(array(":user"=>$user));

	if($cmd->rowCount()>0)
	{
		foreach($cmd as $user)
		{
			if(!checkPassword($pass, $user['password']))
			{
				$token = generateToken();
				storeToken($user['user_id'], $token, $pdo);
				$result['msg'] = "Login successful";
				$result['code'] = 1;
				$result['token'] = $token;
			}
			else
			{
				$result['msg'] = "Invalid Password";
				$result['code'] = -1;
			}
		}
	}
	else
	{
		$result['msg'] = "Invalid username";
		$result['code'] = -1;
	}

	return $result;
}

function logout($token, $pdo)
{
	$testCredentials = verifyToken($token, $pdo);
	$newToken = generateToken();

	if($testCredentials['code']>0)
	{
		$sql = "SELECT user_id 
			FROM users
			WHERE userToken=:token;";
		$cmd = $pdo->prepare($sql);
		$cmd -> execute(array(":token"=>$token));

		if($cmd->rowCount()>0)
		{
			foreach($cmd as $user)
			{
				storeToken($user['user_id'], $newToken, $pdo);
			}
		}
	}
}

function storeToken($userId, $token, $pdo)
{
	// Stores the generated token in the user table
	
	$curTime = new Datetime('now');
	$sql = "UPDATE users 
		SET userToken=:token, loginTime=:time 
		WHERE user_id=:userID;";
	$cmd = $pdo->prepare($sql);
	$cmd -> execute(array(
				":token"=>$token,
				":time"=>$curTime->format('y-m-d H:i:s'),
				":userID"=>$userId));
}

function verifyToken($token, $pdo)
{
	// Verifies the token exists in the user table
	// and compares the current time against a 
	// timestamp for timeout.

	$curTime = new Datetime('now');
	$curTime = $curTime->format('y-m-d H:i:s');

	$sql = "SELECT loginTime 
		FROM users 
		WHERE userToken=:token;";
	$cmd = $pdo->prepare($sql);
	$cmd -> execute(array(":token"=>$token));

	if($cmd->rowCount()>0)
	{
		foreach($cmd as $user)
		{
			if(($user['loginTime'] - $curTime)<=3000)
			{
				$result['msg'] = "Credentials are valid";
				$result['code'] = 1;
			}
			else
			{
				$result['msg'] = "Invalid credentials";
				$result['code'] = -1;
			}
		}
	}
	else
	{
		$result['msg'] = "Unable to verify username.";
		$result['code'] = -1;

	}

	return $result;
}
?>
