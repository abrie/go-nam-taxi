<?php

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 * Description of notifications
 *
 * @author Jonas Tomanga <celleb@mrcelleb.com>
 */
class notifications {

//put your code here
    public function getTaxi($taxi_id) {
	$sql = "SELECT * FROM taxi_notifications WHERE taxi_id = :id && status = 0 ORDER BY DATE ASC LIMIT 1";
	$result = DB::fetch($sql, array(':id' => $taxi_id));
	if ($result) {
	    $feedback = array('code' => 1, array($result));
	    DB::execute("UPDATE taxi_notifications SET status = 1 WHERE date = :id", array(':id' => $result['date']));
	} else {
	    $feedback = array('code' => 0);
	}
	return $feedback;
    }

    public function getPass($cell) {
	$sql = "SELECT * FROM pass_notifications WHERE pass_id = (SELECT pass_id FROM ussd WHERE mobile_numb = :numb) && status = 0 ORDER BY DATE ASC LIMIT 1";
	$result = DB::fetch($sql, array(':numb' => $cell));
	if ($result) {
	    $feedback = array('code' => 1, 'msg' => "You succesfully made a payment of \n\r N$ " . $result['amount'] . " To Taxi " . $result['taxi_id']);
	    DB::execute("UPDATE pass_notifications SET status = 1 WHERE date = :date", array(':date' => $result['date']));
	} else {
	    $feedback = array('code' => 0);
	}
	return $feedback;
    }

}
