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
    if ($register->registerOwner()) {
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
		    <form class='form-horizontal' method='post' action='register.php'>
			<fieldset>
			    <legend>Register Owners</legend>
			    <div class='form-group'>
				<label for='name'>Owner Name:</label>
				<input type='text' class='form-control'name='name' id='name' placeholder='Owner name:' required/>
			    </div>
			    <div class='form-group'>
				<label for='surname'>Owner Surname:</label>
				<input type='text' name='surname' class='form-control' id='surname' placeholder='Owner Surname:' required/>
			    </div>
			    <div class='form-group'>
				<label for='surname'>Owner City:</label>
				<input type='text' name='city' class='form-control' id='city' placeholder='Owner City:' required/>
			    </div>
			    <div class='form-group'>
				<label for='id_no'>Owner ID No:</label>
				<input type='text' name='id_no' class='form-control' id='id_no' placeholder='Owner ID No:' required/>
			    </div>
			    <div class='form-group'>
				<label for='password'>Owner Pin:</label>
				<input type='password' name='password' class='form-control' id='name' placeholder='Owner Pin:' required/>
			    </div>
			    <div class='form-group'>
				<label for='repass'>Re-enter Owner Pin:</label>
				<input type='password' name='repass' class='form-control' id='repass' placeholder='Re-enter Owner Pin:' required/>
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

