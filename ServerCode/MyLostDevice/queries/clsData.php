<?php
class clsData{

	private $connection;

	public function __construct(){
		$con_server = "localhost";
		$con_user = "user";
		$con_pass = "pass";
		$con_db="db";

		$this->connection = mysql_connect($con_server,$con_user,$con_pass) or die("Cannot connect to server");
		mysql_select_db($con_db, $this->connection) or die("Cannot select DB");
	}

	public function __destruct(){
	}

	//Select
	public function queryFilter($sql){
		$resultQuery = mysql_query($sql, $this->connection);
		return $resultQuery;
	}

	//Liberar memoria
	public function closeFilter($resultQuery){
		mysql_free_result($resultQuery);
	}

	//resultado en un array
	public function arrayData($resultQuery){
		$arrayData = mysql_fetch_array($resultQuery);
		return $arrayData;
	}

	//Update, Insert, Delete
	public function queryExecute($sql){
		mysql_query($sql, $this->connection);		
	}

	public function getLastID(){
		return mysql_insert_id();
	}

	public function closeConnection(){
		mysql_close($this->connection);
	}
}

?>
