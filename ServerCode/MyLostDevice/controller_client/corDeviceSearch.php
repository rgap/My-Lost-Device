<?php

	require_once("../queries/clsDevice.php");

	$userid = $_POST['userid'];
	$devid = $_POST['devid'];

        $resp = "-1";
        
        if($userid!=null && $devid!=null){
            
            $objDevice = new clsDevice();

            if(!$objDevice->actionSearch($userid,$devid))
                $resp = "0";
            else
                $resp = "1";
        }

	$data = array('resp' => $resp); 
    
	print (json_encode($data)); 

?>
