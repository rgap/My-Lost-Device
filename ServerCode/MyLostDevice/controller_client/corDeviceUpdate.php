<?php

	require_once("../queries/clsDevice.php");

	$userid = $_POST['userid'];
	$devid = $_POST['devid'];
	$devlocation = $_POST['devlocation'];
	$devstate = $_POST['devstate'];

        $resp = "-1";
        
        if($userid!=null && $devid!=null && $devlocation!=null && $devstate!=null){
            
            $objDevice = new clsDevice();

            $objDevice->actionUpdate($userid,$devid,$devlocation,$devstate);

            $resp = "1";
        }

	print $resp;

?>
