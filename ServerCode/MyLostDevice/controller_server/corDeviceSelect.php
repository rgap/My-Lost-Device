<?php

	require_once("../queries/clsDevice.php");

	$objDevice = new clsDevice();

	$deviceList = $objDevice->actionSelectByUser($userid);

?>
