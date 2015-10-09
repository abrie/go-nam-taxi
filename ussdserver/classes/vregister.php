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
class vregister {

//put your code here
    public $error_message, $success;
    private $owner_id, $vreg, $vmodel, $nplate, $taxi_code, $seats;

    public function __construct() {
	$this->owner_id = trim(strip_tags($_POST['owner_id']));
	$this->vreg = trim(strip_tags($_POST['vreg']));
	$this->nplate = trim(strip_tags($_POST['nplate']));
	$this->taxi_code = trim(strip_tags($_POST['taxi_code']));
	$this->seats = trim(strip_tags($_POST['seats']));
	$this->vmodel = trim(strip_tags($_POST['vmodel']));
	/* validate */
    }

    public function registerVehicle() {
	if (empty($this->error_message)) {

	    $sql = "INSERT INTO vehicles (owner_id, car_reg, model, nplate, taxi_code, seats) VALUES(:id, :vreg, :model, :nplate, :code, :seats)";
	    if (DB::execute($sql, array(':id' => $this->owner_id, ':vreg' => $this->vreg, ':model' => $this->vmodel, ':nplate' => $this->nplate, ':code' => $this->taxi_code, ':seats' => $this->seats))) {
		$this->success = "Vehicle successfully registered, TAXI ID = <strong>{$this->getTaxiId()}</strong>";
		return TRUE;
	    } else {
		$this->error_message['register'] = "Vehicle could not be registered";
		return FALSE;
	    }
	} else {
	    $this->error_message['register'] = "Correct the errors";
	    return FALSE;
	}
    }

    private function getTaxiId() {
	$sql = "SELECT taxi_id FROM vehicles WHERE car_reg = :id LIMIT 1";
	$id = DB::fetch($sql, array(':id' => $this->vreg));
	//print_r($id);
	return $id ? $id['taxi_id'] : FALSE;
    }

}
