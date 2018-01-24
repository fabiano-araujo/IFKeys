<?php 
	$key = "-k^(CA>lU!j[Xc#";	
	function getConection(){		
		$host = "localhost";
		$dbname = "ifkeys";
		$username = "root";
		$password = "";		

	  	$pdo = new PDO('mysql:host='.$host.';dbname='.$dbname, $username, $password,   array(PDO::MYSQL_ATTR_INIT_COMMAND => "SET NAMES utf8"));	  	
		  	//$pdo->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);	  	
	  	return $pdo;
	}	
 ?>