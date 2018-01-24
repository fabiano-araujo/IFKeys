<?php  	
	include 'model/Reserva.php';
	$ok = array('success' => false, "error" => "no data");            
	try {		
			
		
			if (isset($_POST["operation"])) {
				$op = $_POST["operation"];				
				$reserva = getReserva();
				if ($op == 0) {
					$reserva->create();
				}elseif ($op == 1) {
					$reserva->edite();
				}elseif ($op == 2) {										
					$reserva->read();
				}elseif ($op == 3) {
					$reserva->delete();
				}elseif($op == 4){
					$reserva->readEndereco();
				}							
			}  
		
	} catch(PDOException $e) {	  	
	  	$ok = array('success' => false, "error" => $e->getMessage());        
	}  
	function getReserva(){		
		$reserva = new Reserva();				  		  		
		$reserva->id_reserva = isset($_POST["id"]) ? $_POST["id"]: null;
		$reserva->dt_start = date("Y-m-d-h-m");
		$reserva->dt_end = isset($_POST["dt_end"]) ? $_POST["dt_end"]: null;
		$reserva->id_sala = isset($_POST["id_sala"]) ? $_POST["id_sala"]: null;
		$reserva->usuario = isset($_POST["usuario"]) ? $_POST["usuario"]: null;		
		return $reserva;
	}      	
?>