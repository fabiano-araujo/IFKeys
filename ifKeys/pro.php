<?php 
	$now = time(); // or your date as well
	echo date("Y-m-d-h-m")."<br>";
	$your_date = strtotime("2018-01-17-5-1");
	$datediff = $now - $your_date;
	$days = floor($datediff / (60 * 60 * 24));

	$data = isset($_GET["data"]) ? $_GET["data"]: null;
	$money = isset($_GET["money"]) ? $_GET["money"]: null;

	$myfile = fopen("newfile.txt", "w") or die("Unable to open file!");

	
	fwrite($myfile, $data."\n".$money);	
	fclose($myfile);

	
?>