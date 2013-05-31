<?php
require_once("../queries/clsData.php");
class clsDevice{

	private $devid;
	private $userid;
	private $devtype;
	private $devlocation;
	private $devstate;
	private $devcreated;

	public function __construct(){
	}

	//CRUD

	public function actionSelectByUser($userid){
		
		$sql = "select * from DEVICE where (userid=$userid and devstate!=2)";
                
                $objData = new clsData();

		$resultQuery = $objData->queryFilter($sql);
	
		return $resultQuery;
	}

	public function actionSearch($userid, $devid){

		$found = false;

		$objData = new clsData();
		
		$sql = "select * from DEVICE where (userid=$userid and devid=$devid and devstate!=2)";
		$resultQuery = $objData->queryFilter($sql);

		if($row = $objData->arrayData($resultQuery)){
			
			$found = true;
		}

		$objData->closeFilter($resultQuery);
		$objData->closeConnection();

		return $found;
	}

	public function actionInsert($userid,$devtype,$devlocation,$devcreated){

		$objData = new clsData();

		$sql = "insert into DEVICE (userId,devType,devLocation,devState,devCreated) values ($userid,'$devtype','$devlocation',0,'$devcreated')";

		$objData->queryExecute($sql);

		$lastID = $objData->getLastID();

		$objData->closeConnection();

		return $lastID;
	}

	public function actionUpdate($userid, $devid, $devlocation, $devstate){

		$objData = new clsData();

		$sql = "update DEVICE set devlocation='$devlocation', devstate='$devstate' where(userid=$userid and devid=$devid)";
		$objData->queryExecute($sql);
		$objData->closeConnection();
	}

	public function actionDelete(){

		$objData = new clsData();

		$sql = "delete from DEVICE where (devid='$this->devid')";
		$objData->queryExecute($sql);
		$objData->closeConnection();
	}

	public function __destruct(){
	}

}


?>
