<?php

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 * Description of users
 *
 * @author Jonas Tomanga <celleb@mrcelleb.com>
 */
class users {

//put your code here
    private $DB;

    public function __construct() {

    }

    public function login() {
	$username = trim(strip_tags($_POST['username']));
	$password = md5(trim($_POST['password']));
	echo $password;
	$sql = "SELECT user_id, user_type, username FROM users WHERE username = :name && password = :pass LIMIT 1";
	$feedback = DB::fetch($sql, array(':name' => $username, ':pass' => $password));
	print_r($feedback);
	if ($feedback) {
	    $_SESSION['auth'] = $feedback;
	    header("location: register.php");
	} else {
	    return FALSE;
	}
    }

    public function login_status() {
	return isset($_SESSION['auth']) ? TRUE : FALSE;
    }

    public function logged() {
	if ($this->login_status()) {
	    $feedback = "Logged in as " . $_SESSION['auth']['username'] . " | <a href='logout.php'>Logout</a>";
	} else {
	    $feedback = "";
	}
	return $feedback;
    }

}
