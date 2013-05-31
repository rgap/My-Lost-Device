<?php

	require_once("../queries/clsDevice.php");

	$userid = $_POST['userid'];

	$objDevice = new clsDevice();

	$deviceList = $objDevice->actionSelectByUser($userid);

	$rows = array();
	while($r = mysql_fetch_assoc($deviceList)) {
		 $rows[] = $r;
	}
	
	print json_encode($rows);
?>
