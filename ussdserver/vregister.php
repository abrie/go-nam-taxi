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
    $register = new vregister();
    if ($register->registerVehicle()) {
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
		    </div>";
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
	<title>Logic++ eCab - Vehicle Registration</title>
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
			    <legend>Register Vehicle</legend>
			    <div class='form-group'>
				<label for='owner_id'>Owner ID:</label>
				<input type='text' class='form-control'name='owner_id' id='owner_id' placeholder='Owner ID:' required/>
			    </div>
			    <div class='form-group'>
				<label for='vreg'>Vehicle Registration:</label>
				<input type='text' name='vreg' class='form-control' id='vreg' placeholder='Vehicle Registration:' required/>
			    </div>
			    <div class='form-group'>
				<label for='nplate'>Vehicle Number Plate:</label>
				<input type='text' name='nplate' class='form-control' id='nplate' placeholder='Number Plate:' required/>
			    </div>
			    <div class='form-group'>
				<label for='vmodel'>Vehicle Model:</label>
				<input type='text' name='vmodel' class='form-control' id='vmodel' placeholder='Vehicle Model:' required/>
			    </div>
			    <div class='form-group'>
				<label for='taxi_code'>Taxi Code:</label>
				<input type='text' name='taxi_code' class='form-control' id='name' placeholder='Taxi Code:' required/>
			    </div>
			    <div class='form-group'>
				<label for='seats'>Max Seats:</label>
				<input type='text' name='seats' class='form-control' id='seats' placeholder='Maximum Seats:' required/>
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

