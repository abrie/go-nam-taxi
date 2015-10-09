<?php
/* autoloading classes */

function my_autoloader($class) {
    require_once 'classes/' . $class . '.php';
}

spl_autoload_register('my_autoloader');
$page = new html();
$user = new users;
$login_msg = "";
if (isset($_POST['login']) && !$user->login_status()) {
    if (!$user->login()) {
	$login_msg = "Invalid login, Please try again";
    }
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
		<div class='col-md-8'>
		    <div class='panel panel-primary'>
			<div class='panel-heading'>
			    <h3 class='panel-title'>Welcome</h3>
			</div>
			<div class='panel-body'>

			</div>
		    </div>
		</div>
		<div class='col-md-4'>
		    <div class='panel panel-primary'>
			<div class='panel-heading'>
			    <h3 class='panel-title'>Login</h3>
			</div>
			<div class='panel-body'>
			    <form method='post'>
				<?= $login_msg ? "<div class='alert alert-danger alert-dismissible' role='alert'>
				    <button type='button' class='close' data-dismiss='alert' aria-label='Close'>
				    <span aria-hidden='true'>&times;</span></button>
					<strong>Warning!</strong>{$login_msg}.
				    </div>" : '';
				?>
				<div class="form-group">
				    <label for="username">User Name:</label>
				    <input type="text" class="form-control" id="username" name="username" placeholder="Username">
				</div>
				<div class="form-group">
				    <label for="password">Password:</label>
				    <input type="password" class="form-control" id="password" name="password" placeholder="Password">
				</div>
				<center><input type="submit" name='login' value='Login' class="btn btn-default"/></center>
			    </form>
			</div>
		    </div>
		</div>
	    </div>
	    <?= $page->footer(); ?>
	</div>
    </body>
</html>
