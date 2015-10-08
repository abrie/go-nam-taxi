<?php

/**
 * Description of api
 *
 * @author Jonas Tomanga <celleb@mrcelleb.com>
 */
class api {

    public function checkNumber($cell) {
	$sql = "SELECT mobile_numb FROM ussd where mobile_numb = :cell;";
	$return = DB::fetch($sql, array(':cell' => $cell));
	return $return ? $return['mobile_numb'] : false;
    }

    public function checkTaxiCode($taxi) {
	$sql = "SELECT taxi_id FROM vehicles where taxi_id = :taxi;";
	$return = DB::fetch($sql, array(':taxi' => $taxi));
	return $return ? $return['taxi_id'] : false;
    }

    public function getTaxiSeats($taxi) {
	$sql = "SELECT seats FROM vehicles where taxi_id = :taxi;";
	$return = DB::fetch($sql, array(':taxi' => $taxi));
	return $return['seats'];
    }

    public function validatePIN($pin) {
	$sql = "SELECT pass_id FROM ussd where pin = :pin;";
	$return = DB::fetch($sql, array(':pin' => md5($pin)));
	return $return ? $return['pass_id'] : false;
    }

    public function checkBalance($cell) {
	$sql = "SELECT p.balance FROM passenger_balances p JOIN ussd u ON p.pass_id = u.pass_id where u.mobile_numb = :cell;";
	$return = DB::fetch($sql, array(':cell' => $cell));
	return $return ? $return['balance'] : false;
    }

    public function checkSession($cell) {
	$sql = "SELECT stage FROM session where mobile_number =  :cell;";
	$return = DB::fetch($sql, array(':cell' => $cell));
	return $return ? $return['stage'] : false;
    }

    public function addSession($session) {
	$sql = "INSERT INTO session (mobile_number, taxi_id, amount, seats, stage) VALUES (:mobile_number, :taxi_id, :amount, :seat, :stage)";
	DB::execute($sql, $session);
    }

    public function updateSession($cell) {
	$sql = "UPDATE session SET stage = 1, status = status + 1 WHERE mobile_number = :cell ";
	DB::execute($sql, array(':cell' => $cell));
    }

    public function deleteSession($cell) {
	$sql = "DELETE FROM session WHERE mobile_number = :cell";
	return DB::execute($sql, array(':cell' => $cell)) ? TRUE : FALSE;
    }

    public function getSession($cell) {
	$sql = "SELECT * FROM session WHERE mobile_number = :cell";
	$session = DB::fetch($sql, array(':cell' => $cell));
	return $session;
    }

    public function executeTransacton($cell) {
	$ussdInfo = $this->getSession($cell);
	if ($ussdInfo) {
	    $sql = "INSERT INTO passenger_accounts (taxi_id, method, debit, pass_id) "
		    . "VALUES(:tid, 'USSD', :mula, (SELECT pass_id FROM ussd WHERE mobile_numb = :numb));"
		    . " UPDATE passenger_balances SET total_debit = total_debit + :mula, balance = balance - :mula; "
		    . "WHERE pass_id = (SELECT pass_id FROM ussd WHERE mobile_numb = :numb)";
	    if (DB::execute($sql, array(':tid' => $ussdInfo['taxi_id'], ':mula' => $ussdInfo['amount'], ':numb' => $cell))) {
		$sql = "INSERT INTO taxi_accounts (trans_id, taxi_id, method, credit, pass_id) "
			. "VALUES(:trans,:tid, 'USSD', :mula, (SELECT pass_id FROM ussd WHERE mobile_numb = :numb));"
			. " UPDATE taxi_balances SET total_credit = total_credit + :mula, balance = balance + :mula; "
			. "WHERE pass_id = (SELECT pass_id FROM ussd WHERE mobile_numb = :numb) LIMIT 1";
		if (DB::execute($sql, array(':tid' => $ussdInfo['taxi_id'], ':mula' => $ussdInfo['amount'], ':numb' => $cell, ':trans' => $this->getTransid($cell, $ussdInfo['taxi_id'])))) {
		    if ($this->updateNotifications($ussdInfo, $cell)) {
			$this->deleteSession($cell);
			return $ussdInfo;
		    }
		}
	    }
	}
    }

    public function loginAttempts($cell) {
	$sql = "SELECT status FROM ussd WHERE mobile_number = :cell LIMIT 1";
	$result = DB::fetch($sql, array(':cell' => $cell));
	return $result['status'];
    }

    public function getTransid($cell, $tid) {
	$sql = "SELECT trans_id FROM passenger_accounts WHERE pass_id = (SELECT pass_id FROM ussd WHERE mobile_numb = :numb) && taxi_id = :tid ORDER BY tdate DESC LIMIT 1";
	$transid = DB::fetch($sql, array(':numb' => $cell, ':tid' => $tid));
	//var_dump($transid) . "vvv";
	return $transid ? $transid['trans_id'] : FALSE;
    }

    private function getPassId() {

    }

    private function updateNotifications($info, $cell) {
	/* session */
	$sql = "INSERT INTO taxi_notifications (taxi_id, seat, amount) VALUES(:tid, :seat, :amount)";
	DB::execute($sql, array(':tid' => $info['taxi_id'], ':seat' => $info['seats'], ':amount' => $info['amount']));
	$sql = "INSERT INTO pass_notifications (pass_id, taxi_id, seat, amount) VALUES((SELECT pass_id FROM ussd WHERE mobile_numb = :numb), :tid, :seat, :amount)";
	return DB::execute($sql, array(':numb' => $cell, ':tid' => $info['taxi_id'], ':seat' => $info['seats'], ':amount' => $info['amount'])) ? TRUE : FALSE;
    }

}
