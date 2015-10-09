<?php

/**
 * Description of helper
 *
 * @author Jonas Tomanga <celleb@mrcelleb.com>
 */
class helper {

    static public function url($url, $name, $path = '', $id = '', $classes = '', $other = '') {
	return "<a href='" . $path . $url . "' id='" . $id . "' class='" . $classes . "' " . $other
		. ">" . $name . "</a>";
    }

    static public function getSeats($seats) {
	$group_seats = array();
	for ($i = 0; $i < strlen($seats); $i++) {
	    $group_seats[$i] = (int) $seats[$i];
	}
	$seats = max($group_seats);
	return $seats;
    }

    static public function groupSeats($seats) {
	$group_seats = array();
	for ($i = 0; $i < strlen($seats); $i++) {
	    $group_seats[$i] = (int) $seats[$i];
	}
	return $group_seats;
    }

}
