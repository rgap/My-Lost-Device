<?php

	$username = $_POST['username'];	
	$password = $_POST['password'];
        
     $resp = "0";
     
     if($username!=null && $password!=null){

         require_once("../queries/clsUser.php");

         $objUser = new clsUser();

         if(!$objUser->actionCheck($username)){
                 $resp = $objUser->actionInsert($username,$password);
         }
     }

    $data = array('resp' => $resp); 
    
	print (json_encode($data)); 

	
		
?>
