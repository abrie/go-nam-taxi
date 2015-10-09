<?php

/* autoloading classes */

function my_autoloader($class) {
    require_once 'classes/' . $class . '.php';
}

spl_autoload_register('my_autoloader');
$string = isset($_POST['JSON']) ? json_decode($_POST['JSON']) : json_decode(json_encode(array('cell' => '0853478687', 'code' => '1')));
$code = trim($string->code);
if (preg_match("/^\*145\*\d+\*\d+\*\d{1,7}\#$/", $code)) {
    $code = str_replace(array('*145*', '#'), '', $code);
    $code = split('\*', $code);
    if (preg_match('/^.*(.).*\1.*$/', $code[2])) {
	$status = 0;
    } else {
	// if the code is valid
	$session = array(':mobile_number' => $string->cell, ':taxi_id' => (int) $code[0], ':amount' => (float) $code[1], ':seat' => (int) $code[2], ':stage' => 0);

	//Check if session exits
	$call = new api;

	$stage = $call->checkSession($string->cell);
	if ($stage === false) {
	    //validate phone number
	    $cell = $call->checkNumber($string->cell);

	    if ($cell === false) {
		$feedback = array('code' => 0, 'msg' => "You are not registered for eCab \n\r");
	    } else {
		//Validate taxi_id
		$taxi = $call->checkTaxiCode($code[0]);

		if ($taxi === false) {
		    $feedback = array('code' => 0, 'msg' => "Invalid Taxi Code");
		} else {
		    //check seats against registered vehicle
		    $seats = $call->getTaxiSeats($taxi);
		    // echo $seats;
		    $cseats = (strlen($code[2]) > 1) ? helper::getSeats($code[2]) : $code[2];
		    if ($cseats > $seats) {
			$feedback = array('code' => 0, 'msg' => "Invalid Seat Number");
		    } else {

			//check balance
			$balance = $call->checkBalance($string->cell);
			if ($balance >= $code[1]) {
			    //create new session
			    $call->addSession($session);
			    $feedback = array('code' => 1, 'msg' => "Welcome to eCab \n\r Please enter your pin\n\r");
			} else {
			    $feedback = array('code' => 0, 'msg' => "Insufficient funds \n\r Thank you");
			}
		    }
		}
	    }
	} else {
	    $feedback = array('code' => 0, 'msg' => "Welcome to eCab  \n\r Invalid request \n\r");
	}

	//if not create session
    }
    //print_r($code);
} else {

    $call = new api;
    $stage = $call->checkSession($string->cell);
    if ($stage === false) {
	$feedback = array('code' => 0, 'msg' => "Welcome to eCab  \n\r Invalid request \n\r");
    } else {
	//check pin
	switch ($stage) {
	    case 0 :
		//validate pin
		$pin = $call->validatePIN($code);
		//  echo '<br/>'. md5((int)trim($code))."<<<";

		if ($pin === false) {
		    $attempts = $call->loginAttempts($string->cell);
		    if ($attempts > 3) {
			$feedback = array('code' => 1, 'msg' => "Invalid Pin Please Try Again! \n\r" . ($attempts - 3) . " Attempts left.");
		    } else {
			$feedback = array('code' => 0, 'msg' => "Too many invalid attempt \n\r Account blocked");
		    }
		} else {
		    $info = $call->getSession($string->cell);
		    $feedback = array('code' => 1, 'msg' => "Please confirm you want to pay \n N$ {$info['amount']} \n To Taxi#: {$info['taxi_id']}? \n\r 1. Yes \n\r 2. No");
		    // update session
		    $call->updateSession($string->cell);
		}
		break;
	    case 1:
		//process trans
		switch ($code) {
		    case '1': // transfer
			$info = $call->executeTransacton($string->cell);
			if ($info) {
			    $feedback = array('code' => 0, 'msg' => "You have successfuly paid \n\r N$ " . $info['amount'] . "\n\r Seat No: " . $info['seats'] . "\n\r To Taxi #: " . $info['taxi_id']);
			} else {
			    $feedback = array('code' => 0, 'msg' => "Oops sorry, an error occured\n\r");
			    //var_dump(DB::DBi()->errorInfo());
			    $call->deleteSession($string->cell);
			}
			break;
		    default : $feedback = array('code' => 0, 'msg' => "Transaction cancelled \n\r");
			$call->deleteSession($string->cell);
			break;
		}
		break;
	}
    }
}
header('Content-Type: application/json');
echo json_encode($feedback);
