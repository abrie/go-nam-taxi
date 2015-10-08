<?php

/**
 * Description of database
 *
 */
/* database abstraction class */
class dBase {

    protected $dsn = 'mysql:host=localhost;dbname=logic';

    const Username = 'celleb';
    const Password = 'celebrity';

    public function conn() {
	return new PDO($this->dsn, self::Username, self::Password);
    }

}
