<?php
	include 'conection.php';	
	class Sala{	
		public $id_sala;
	    public $nome;
    	public $numero;
    	public $disponivel;
    	public $campus;   
    	public $pdo;

       
     function create(){		
     	$pdo = getConection();
		$stmt = $pdo->prepare("SELECT id_sala FROM sala where nome = :nome and numero = :numero;");		 		

		$stmt->execute(array(		    				
		    ':nome' => $this->nome,
		    ':numero' => $this->numero
		));	   		
		if ($stmt->fetch()[0] == null) {
			$stmt = $pdo->prepare("INSERT INTO sala(nome,numero,disponivel,campus)
	  	 						VALUES (:nome,:numero,:disponivel,:campus)");			
	  		$stmt->execute(array(		    				
			    ':nome' => $this->nome,
			    ':numero' => $this->numero,
	    		':disponivel' => $this->disponivel,
	    		':campus' => $this->campus,	    		
			));	   			
	  		$ok = array('success' => true, 'id_sala' => $pdo->lastInsertId());            	  		   
		}else{
			$ok = array('success' => false, "error" => 0);		
		}				  			  		
		echo json_encode($ok); 
	}	
		
	function read(){		
		try {
			$pdo = getConection();
			$limit = isset($_POST["limit"]) ? $_POST["limit"]: 10;
			$index = isset($_POST["index"]) ? $_POST["index"]: 0;	

			$w = "where 1 = 1";		
			$w .= " and campus = '".$this->campus."'";		
			$w .= " and disponivel = '".$this->disponivel."'";	
			echo $w;
			
			if ($this->disponivel) {
				$consulta = $pdo->query("SELECT 
					id_sala, 
					nome,
					numero,
					disponivel,
					campus 				
					FROM sala $w LIMIT $limit OFFSET $index;");					
				$c = $consulta->fetchAll(PDO::FETCH_CLASS);					
				$json = array();			
				foreach ($c as $key => $value) {				
					array_push($json, array(
						'id_sala' => $value->id_sala,
						'nome' => $value->nome,
						'numero' => $value->numero,
						'disponivel' => $value->disponivel,
						'campus' => $value->campus									
					));							
				}		
				$ok = array('success' => true, 'salas' =>$json);		
			}else{
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
			}
						
					
		} catch (Exception $e) {
			$ok = array('success' => false, "error" => $e->getMessage());        			
		}		
		echo json_encode($ok);               			
	}	
}	
 ?>