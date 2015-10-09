<?php

/**
 * The class for page construction
 * @author Jonas Tomanga
 * @copyright (c) 2013, Namibian Businesses Online
 */
class html {

    private $favicon, $htmlheader, $meta, $char;
    private $path;

    /**
     * Set the default properties of the page
     * @param string $favicon The page's favicon icon (optional)
     * @param array $meta A multidimensional array of meta tags e.g array('name' => 'content')
     * @param string $char Character encoding
     * @param string $path The Relative path of the page
     */
    public function __construct($meta = array('author' => "Logic"), $favicon = 'favicon.ico', $char = 'UTF-8') {
	$this->favicon = $favicon;
	$this->meta = $meta;
	$this->char = $char;
	$this->startSession();
    }

    public function setPath($path) {
	$this->path = $path;
    }

    private function startSession() {
	session_start();
    }

    /**
     * Assembles the html page header and returns $htmlheader
     * @return string Returns a string
     */
    public function htmlHeader() {
	date_default_timezone_set('Africa/Windhoek');
	$this->htmlheader .= "<meta http-equiv='content-type' content='text/html; charset=" . $this->char . "'/>\n";
	$this->htmlheader .= "<link rel='shortcut icon' href='{$this->path}" . $this->favicon . "' type='image/x-icon'/>\n";
	foreach ($this->meta as $key => $value) {
	    $this->htmlheader .= "<meta name='" . $key . "' content='" . $value . "'/>\n";
	}
	return $this->htmlheader;
    }

    /**
     * Constructs the page header ready for html output
     * @param array $currentLink Array containing the links of the main navigation bar
     * @param string $links The name of the current link
     * @return string Returns an html formated string ready for output
     */
    public function mainNav($currentLink = 'Logic++', $links = array('Passenger Register' => 'pregister', 'Owner Register' => 'register', 'Vehicle Register' => 'vregister')) {
	$header = "<nav class='navbar navbar-default'>
    <div class='container'>";
	$header .= "<div class='navbar-header'><a class='navbar-brand' href='{$this->path}./'>Logic++</a></div>";
	$header.="<div class='navbar-collapse'>
	    <ul class='nav navbar-nav'>";
	foreach ($links as $key => $value) {
	    if (!empty($_SESSION['user']) && ($key == 'Login')) {

	    } else {
		$header .= "<li><a href='{$this->path}{$value}'";

		$header.= ($key === $currentLink) ? " class='active-link' " : "";

		$header.= " > {$key} </a></li>";
	    }
	}
	return $header . "</ul></div></div></nav>";
    }

    public function footer() {
	$date = new DateTime();
	$year = $date->format('Y');
	return "<div class='row'>
		    <div class='panel panel-default'>
			<div class='panel-body'>
			    <a href='./'>Home</a> | <a href='contacts.php'>Contact us</a> | <a href='about.php'>About Us</a>
			</div>
		    <div class='panel-footer'><p align='center'>Copyright &copy; {$year} Logic++</p></div>
		    </div>
		</div>";
    }

}
