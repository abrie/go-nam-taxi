<?php

/* autoloading classes */

function my_autoloader($class) {
    require_once 'classes/' . $class . '.php';
}

spl_autoload_register('my_autoloader');
echo md5("celebrity");
echo '<br />';
echo md5("08156");

helper::getSeats('1237');
