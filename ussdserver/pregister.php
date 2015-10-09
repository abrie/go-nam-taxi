<?php
/* autoloading classes */

function my_autoloader($class) {
    require_once 'classes/' . $class . '.php';
}

spl_autoload_register('my_autoloader');
$page = new html();
$user = new users;
$login_msg = "";
if (!$user->login_status()) {
    header('location: ./?status=login');
}
if (isset($_POST['register'])) {
    $register = new register();
    if ($register->registerPassengers()) {
	$feedback = "<div class='alert alert-success alert-dismissible' role='alert'>
			<button type='button' class='close' data-dismiss='alert' aria-label='Close'>
			<span aria-hidden='true'>&times;</span></button>
		    <strong></strong>{$register->success}.
		    </div>";
    } else {
	$feedback = "<div class='alert alert-danger alert-dismissible' role='alert'>
			<button type='button' class='close' data-dismiss='alert' aria-label='Close'>
			<span aria-hidden='true'>&times;</span></button>
			<strong>Warning! </strong>{$register->error_message['register']}.
		    </div>" . var_dump($register->error_message);
    }
} else {
    $feedback = "";
}
?>
<!DOCTYPE html>
<html lang="en">
    <head>
	<?php include_once 'html/default_css_js.html'; ?>
	<meta charset="utf-8"/>
	<meta http-equiv="X-UA-Compatible" content="IE=edge"/>
	<meta name="viewport" content="width=device-width, initial-scale=1"/>
	<title>Logic++ eCab</title>
	<?= $page->htmlHeader(); ?>
    </head>
    <body>
	<div class='container'>
	    <div class='row'>
		<?= $user->logged(); ?>
		<?= $page->mainNav(); ?>
		<div class='col-md-8 col-md-offset-2'>
		    <?= $feedback; ?>
		    <form class='form-horizontal' method='post'>
			<fieldset>
			    <legend>Register Passengers</legend>
			    <div class='form-group'>
				<label for='name'>Name:</label>
				<input type='text' class='form-control'name='name' id='name' placeholder='Passenger name:' required/>
			    </div>
			    <div class='form-group'>
				<label for='surname'>Surname:</label>
				<input type='text' name='surname' class='form-control' id='surname' placeholder='Passenger Surname:' required/>
			    </div>
			    <div class='form-group'>
				<label for='surname'>City:</label>
				<input type='text' name='city' class='form-control' id='city' placeholder='Passenger City:' required/>
			    </div>
			    <div class='form-group'>
				<label for='ussd_no'>USSD Mobile Number:</label>
				<input type='text' name='ussd_no' class='form-control' id='ussd_no' placeholder='i.e. +264810000000' required/>
			    </div>
			    <div class='form-group'>
				<label for='prime'>Primary Contact No:</label>
				<input type='text' name='prime' class='form-control' id='prime' placeholder='i.e. +264810000000' required/>
			    </div>
			    <div class='form-group'>
				<label for='id_no'>ID No:</label>
				<input type='text' name='id_no' class='form-control' id='id_no' placeholder='Passenger ID No:' required/>
			    </div>
			    <div class='form-group'>
				<label for='password'>USSD Pin:</label>
				<input type='password' name='password' class='form-control' id='name' placeholder='Passenger Pin:' required/>
			    </div>
			    <div class='form-group'>
				<label for='repass'>Re-enter USSD Pin:</label>
				<input type='password' name='repass' class='form-control' id='repass' placeholder='Re-enter Passenger Pin:' required/>
			    </div>
			    <div class="form-group">
				<center><input type="submit" class="btn btn-default" value='Register'/></center>
			    </div>
			    <input type='hidden' value='register' name='register'/>
			</fieldset>
		    </form>
		</div>
	    </div>

	    <?= $page->footer(); ?>
	</div>
    </body>
</html>

