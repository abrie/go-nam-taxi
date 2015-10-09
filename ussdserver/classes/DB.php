<?php

/**
 * Provides and interface to call the PDO statically
 * @author Jonas Tomanga <celleb@mrcelleb.com>
 * @copyright (c) 2015, XLAB
 * @version 2.0
 */
class DB {

    /**
     * Holds the PDO statement object
     * @var object
     * @since version 2.0
     */
    private static $stmt;

    /**
     * Creates an object to statically use pdo
     * @param string $user The Database Username
     * @param string $pass The Database password of the given user
     * @return \PDO Returns PHP Database Object
     * @static
     * @since version 1.0
     */
    static public function DBi($user = 'celleb', $pass = 'celebrity') {
	try {
	    $data = 'mysql:host=localhost;dbname=logic';

	    $DB = new PDO($data, $user, $pass);
	    return $DB;
	} catch (PDOException $e) {
	    return self::pdoException($e);
	}
    }

    /**
     * Prepares a statement and executes the statement using the given query and parameters
     * @param string $sql A string of the sql query
     * @param array $params An array of the parameter
     * @todo Add proper exception handling
     * @since version 2.0
     * @static
     */
    static public function execute($sql, $params = array()) {
	/* clear stmt */
	self::$stmt = "";
	try {
	    self::$stmt = self::DBi()->prepare($sql);
	    return self::$stmt->execute($params) ? true : false;
	} catch (PDOException $e) {
	    return self::pdoException($e);
	}
    }

    /**
     * Fetches the results of the executed statement
     * @static
     * @param string $sql A string of the sql query
     * @param array $params An array of the parameter
     * @param int $method The code for the fetch Method to be used, defaulst to FETCH_ASSOC
     * @return array Returns an array of rows from the database
     * @since version 2.0
     */
    static public function fetchAll($sql, $params = array(), $method = PDO::FETCH_ASSOC) {
	try {
	    return self::execute($sql, $params) ? self::$stmt->fetchAll($method) : false;
	} catch (PDOException $e) {
	    return self::pdoException($e);
	}
    }

    /**
     * Fetch the result of the executed statement
     * @static
     * @param string $sql A string of the sql query
     * @param array $params An array of the parameter
     * @param int $method Fetch Method
     * @return array Returns an array of one from the database
     * @since version 2.0
     */
    static public function fetch($sql, $params = array(), $method = PDO::FETCH_ASSOC) {
	try {
	    return self::execute($sql, $params) ? self::$stmt->fetch($method) : FALSE;
	} catch (PDOException $e) {
	    return self::pdoException($e);
	}
    }

    /**
     * Deals with the PDO exceptions for the whole class
     * @param array $e An of the PDO error detaisl
     */
    static private function pdoException($e) {
	$message = "Sorry a Database Error occured " . $e->getCode . " " . $e->getMessage();
	$code = 0;
	return array('code' => $code, 'message' => $message);
    }

}
