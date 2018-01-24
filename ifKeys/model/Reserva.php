<?php
	include 'conection.php';	
	class Reserva{	
		public $id_reserva;
	    public $dt_start;
    	public $dt_end;
    	public $id_sala;
    	public $usuario;   
    	
       
     function create(){		
     	$pdo = getConection();
		$stmt = $pdo->prepare("SELECT id_reserva FROM reserva where nome = :nome and numero = :numero;");		 		
		
		
		$stmt = $pdo->prepare("INSERT INTO reserva(dt_start,dt_end,id_sala,usuario)
  	 						VALUES (:dt_start,:dt_end,:id_sala,:usuario)");			
  		$stmt->execute(array(		    				
		    ':dt_start' => $this->dt_start,
		    ':dt_end' => $this->dt_end,
    		':id_sala' => $this->id_sala,
    		':usuario' => $this->usuario,	    		
		));	   			
  		$ok = array('success' => true, 'id_sala' => $pdo->lastInsertId());            	  		   		
		echo json_encode($ok); 
	}	
		
	function read(){		
		try {
			$pdo = getConection();
			$limit = isset($_POST["limit"]) ? $_POST["limit"]: 10;
			$index = isset($_POST["index"]) ? $_POST["index"]: 0;	

			$w = "where 1 = 1";					
				
			$consulta = $pdo->query("SELECT 
				id_reserva, 
				dt_start,
				dt_end,
				sala.id_sala, 
				nome,
				numero,
				disponivel,
				campus, 	
				usuario 				
				FROM reserva INNER JOIN sala ON sala.id_sala = reserva.id_sala $w LIMIT $limit OFFSET $index;");					
			$c = $consulta->fetchAll(PDO::FETCH_CLASS);					
			$json = array();			
			foreach ($c as $key => $value) {				
				array_push($json, array(
					'id_reserva' => $value->id_reserva,
					'dt_start' => $value->dt_start,
					'dt_end' => $value->dt_end,					
					'sala' => array(
							'id_sala' => $value->id_sala,
							'nome' => $value->nome,
							'numero' => $value->numero,
							'disponivel' => $value->disponivel,
							'campus' => $value->campus	
					),
					'usuario' => $value->usuario									
				));							
			}		
				
			


			$ok = array('success' => true, 'Reservas' =>$json);					
		} catch (Exception $e) {
			$ok = array('success' => false, "error" => $e->getMessage());        			
		}		
		echo json_encode($ok);               			
	}	
}	
 ?>