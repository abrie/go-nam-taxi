<?php

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 * Description of register
 *
 * @author Jonas Tomanga <celleb@mrcelleb.com>
 */
class register {

//put your code here
    public $error_message, $success;
    private $name, $surname, $password, $repassword, $id, $city, $mobile, $primary;

    public function __construct() {
	$this->name = trim(strip_tags($_POST['name']));
	$this->surname = trim(strip_tags($_POST['surname']));
	$this->id = trim(strip_tags($_POST['id_no']));
	$this->password = trim(strip_tags($_POST['password']));
	$this->repassword = trim(strip_tags($_POST['repass']));
	$this->city = trim(strip_tags($_POST['city']));
	/* validate */
    }

    private function validateName() {
	if (!empty($this->name)) {
	    if (strlen($this->name) > 25 || strlen($this->name) < 2) {
		$this->error_message['name'] = "Name should be more than 2 characters long and less than 25 characters";
	    }
	} else {
	    $this->error_message['name'] = "Name is required";
	}
    }

    private function validateSurname() {
	if (!empty($this->surname)) {
	    if (strlen($this->surname) > 25 || strlen($this->surname) < 2) {
		$this->error_message['surname'] = "Surname should be more than 2 characters long and less than 25 characters";
	    }
	} else {
	    $this->error_message['surname'] = "Surname is required";
	}
    }

    private function validateId($table) {
	if (!empty($this->id)) {
	    $sql = "SELECT id_no FROM {$table} WHERE id_no = ?";
	    if (DB::fetch($sql, array($this->id))) {
		$this->error_message['id_no'] = "That id is already registered";
	    }
	    if (strlen($this->id) != 11) {
		$this->error_message['id_no'] = "ID Number should be of 11 digits";
	    }
	    if (!is_numeric($this->id)) {
		$this->error_message['id_no'] = "ID Number is not valid";
	    }
	} else {
	    $this->error_message['id_no'] = "ID number is required";
	}
    }

    private function validatePassword() {
	if (!empty($this->password)) {
	    if (strlen($this->password) != 5) {
		$this->error_message['password'] = "Pin should be 5 characters";
	    }
	    if ($this->password != $this->repassword) {
		$this->error_message['password'] = "Pins do not match";
	    }
	} else {
	    $this->error_message['password'] = "Pin is required";
	}
    }

    private function validateCity() {
	if (!empty($this->city)) {

	} else {
	    $this->error_message['city'] = "City is required";
	}
    }

    public function registerOwner() {
	$this->validateAll('owners');
	if (empty($this->error_message)) {

	    $sql = "INSERT INTO owners (fname, lname, id_no, pin, city, added_by) VALUES(:name, :surname, :id_no, :pin, :city, :added)";
	    if (DB::execute($sql, array(':name' => $this->name, ':surname' => $this->surname, ':id_no' => $this->id, ':pin' => md5($this->password), ':city' => $this->city, ':added' => $_SESSION['auth']['user_id']))) {
		$this->success = "Owner successfully registered, Owner ID = <strong>{$this->getOwnerId()}</strong>";
		return TRUE;
	    } else {
		$this->error_message['register'] = "Owner could not be registered";
		return FALSE;
	    }
	} else {
	    $this->error_message['register'] = "Correct the errors";
	    return FALSE;
	}
    }

    public function validateAll($table) {
	$this->validateCity();
	$this->validateId($table);
	$this->validateName();
	$this->validatePassword();
	$this->validateSurname();
    }

    private function getOwnerId() {
	$sql = "SELECT owner_id FROM owners WHERE id_no = :id LIMIT 1";
	$id = DB::fetch($sql, array(':id' => $this->id));
	//print_r($id);
	return $id ? $id['owner_id'] : FALSE;
    }

    private function getPassId() {
	$sql = "SELECT pass_id FROM passengers WHERE id_no = :id LIMIT 1";
	$id = DB::fetch($sql, array(':id' => $this->id));
	//print_r($id);
	return $id ? $id['pass_id'] : FALSE;
    }

    private function validateUssd() {
	if (!empty($this->mobile)) {
	    if (preg_match("/^\+264\d{9}$/", $this->mobile)) {
		$sql = "SELECT mobil_numb FROM ussd WHERE mobile_numb = ?";
		if (DB::fetch($sql, array($this->mobile))) {
		    $this->error_message['ussd_no'] = "That USSD Mobile Number is already registered";
		}
	    } else {
		$this->error_message['ussd_no'] = "USSD Mobile Number is Not Valid";
	    }
	} else {
	    $this->error_message['ussd_no'] = "USSD Mobile Number is required";
	}
    }

    private function validatePrime() {
	if (!empty($this->primary)) {
	    if (preg_match("/^\+264\d{8,9}$/", $this->primary)) {

	    } else {
		$this->error_message['prime'] = "Primary Contact Number is Not Valid";
	    }
	} else {
	    $this->error_message['prime'] = "Primary Contact Number is required";
	}
    }

    public function registerPassengers() {
	$this->mobile = trim($_POST['ussd_no']);
	$this->primary = trim($_POST['prime']);
	$this->validateAll('passengers');
	$this->validatePrime();
	$this->validateUssd();

	if (empty($this->error_message)) {
	    $sql = "INSERT INTO passengers (fname, lname, id_no, prime_contact, city, added_by) VALUES(:name, :surname, :id_no, :contact, :city, :added)";
	    if (DB::execute($sql, array(':name' => $this->name, ':surname' => $this->surname, ':id_no' => $this->id, ':contact' => $this->primary, ':city' => $this->city, ':added' => $_SESSION['auth']['user_id']))) {
		$sql = "INSERT INTO ussd (pass_id, mobile_numb, pin) VALUES(:id, :mob, :pin)";
		if (DB::execute($sql, array(':id' => $this->getPassId(), ':mob' => $this->mobile, ':pin' => md5($this->password)))) {
		    $this->success = "Passenger successfully registered, PASS ID = <strong>{$this->getPassId()}</strong>";
		    return TRUE;
		} else {
		    $this->error_message['register'] = "Could Not Register USSD";
		    return FALSE;
		}
	    } else {
		$this->error_message['register'] = "Passenger could not be registered";
		return FALSE;
	    }
	} else {
	    $this->error_message['register'] = "Please fix errors";
	    return FALSE;
	}
    }

}
