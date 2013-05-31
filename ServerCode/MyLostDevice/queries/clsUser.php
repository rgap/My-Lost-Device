<?php
require_once("../queries/clsData.php");
class clsUser{

	private $userid;
	private $username;
	private $userpass;

	public function __construct(){
	}

	//CRUD

	public function actionCheck($username){

		$found = false;

		$objData = new clsData();
		$sql = "select * from DUSER where (username='$username')";
		$resultQuery = $objData->queryFilter($sql);

		if($row = $objData->arrayData($resultQuery)){

			$found = true;
		}

		$objData->closeFilter($resultQuery);
		$objData->closeConnection();

		return $found;
	}

	public function actionSearchUser($username, $userpass){

		$found = -1;

		$objData = new clsData();
		$sql = "select * from DUSER where (username='$username' and userpass='$userpass')";
		$resultQuery = $objData->queryFilter($sql);

		if($row = $objData->arrayData($resultQuery)){

			$found = $row['userId'];
		}

		$objData->closeFilter($resultQuery);
		$objData->closeConnection();

		return $found;
	}

	public function actionInsert($username,$userpass){

		$objData = new clsData();

		$sql = "insert into DUSER(username,userpass) values ('$username','$userpass')";
		$objData->queryExecute($sql);

		$lastID = $objData->getLastID();

		$objData->closeConnection();

		return $lastID;
	}

	public function actionUpdate(){

		$objData = new clsData();

		$sql = "update DUSER set username='$this->username', userpass='$this->userpass' where(userid='$this->userid')";
		$objData->queryExecute($sql);
		$objData->closeConnection();
	}

	public function actionDelete(){

		$objData = new clsData();

		$sql = "delete from DUSER where (userid='$this->userid')";
		$objData->queryExecute($sql);
		$objData->closeConnection();
	}

	public function __destruct(){
	}

}


?>
