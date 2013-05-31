<?php

	require_once("../queries/clsUser.php");

	$username = $_POST['username'];	
	$password = $_POST['password'];
        
     $resp = "0";
     
     if($username!=null && $password!=null){

         $objUser = new clsUser();

         $resCons = $objUser->actionSearchUser($username,$password);

         if($resCons != -1)
             $resp = $resCons;
     }

	$data = array('resp' => $resp); 
    
	print (json_encode($data)); 
		
?>
