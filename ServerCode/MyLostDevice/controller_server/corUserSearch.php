<?php

    $userid = -1;

    require_once("../queries/clsUser.php");

    $username = isset($_POST['username']) ? $_POST['username'] : null;
    $password = isset($_POST['password']) ? $_POST['password'] : null;

	 if($username == null && $password == null){
		 $username = isset($_GET['username']) ? $_GET['username'] : null;
		 $password = isset($_GET['password']) ? $_GET['password'] : null;
	 }

    if($username != null && $password != null){

        $objUser = new clsUser();

        $userid = $objUser->actionSearchUser($username,$password);
    }

    if($userid == -1){

        echo "<script>
                alert('Wrong Username or Password');
        </script>";

    }
		
?>
