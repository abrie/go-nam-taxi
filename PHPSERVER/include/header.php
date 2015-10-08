<nav class='navbar navbar-default'>
    <div class='container'>
	<div class='navbar-header'>
	    <a class='navbar-brand' href='./'>CT</a>
	</div>
	<div class='navbar-collapse'>
	    <ul class='nav navbar-nav'>
		<li><a href='items.php'>Items</a></li>
		<li><a href='about.php'>About us</a></li>
		<li><a href='contact.php'>Contact us</a></li>
		<?php
		if (!isset($_SESSION['user'])) {
		    echo "<li><a href='login.php'>login</a></li>";
		    echo "<li><a href='register.php'>Register</a></li>";
		}
		if (isset($_SESSION['cart'])) {
		    $total = count($_SESSION['cart']);
		    echo "<li><a href='cart.php'>Cart <span class='badge'>{$total}</span></a></li>";
		    echo "<li><a href='checkout.php'>Checkout</a></li>";
		}
		?>
		<?php
		if (isset($_SESSION['user']) && $_SESSION['user']['account_type'] == 'admin') {
		    echo "<li><a href='admin.php'>Admin</a></li>";
		}
		?>
	    </ul>
	    <?php
	    if (isset($_SESSION['user'])) {
		echo "<p class='navbar-text navbar-right'>Signed in as <a href='account.php' class='navbar-link'>{$_SESSION['user']['user_name']}</a> | <a href='login.php?action=logout'>Logout</a></a></p>";
	    }
	    ?>
	</div>
    </div>
</nav>

