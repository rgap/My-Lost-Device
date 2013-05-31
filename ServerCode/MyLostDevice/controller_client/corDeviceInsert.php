<?php

	require_once("../queries/clsDevice.php");

	$userid = $_POST['userid'];
	$devtype = $_POST['devtype'];
	$devlocation = $_POST['devlocation'];

        
        $resp = "-1";
        
        if($userid!=null && $devtype!=null && $devlocation!=null){
            
            $devcreated = date("Y-m-d H:i:s");

            $objDevice = new clsDevice();

            $resp = $objDevice->actionInsert($userid,$devtype,$devlocation,$devcreated);

        }

	$data = array('resp' => $resp); 
    
	print (json_encode($data)); 
	   
?>
