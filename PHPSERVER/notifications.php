<?php

function my_autoloader($class) {
    require_once 'classes/' . $class . '.php';
}

spl_autoload_register('my_autoloader');

$string = json_decode($_POST['JSON']);

$not = new notifications();

switch ($string->code) {
    case 0:
	$feedback = $not->getPass($string->cell);
	break;
    case 1:
	$feedback = $not->getTaxi($string->taxiid);
	break;
}

header('Content-Type: application/json');
echo json_encode($feedback);
