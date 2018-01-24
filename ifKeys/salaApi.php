<?php  	
	include 'model/Sala.php';
	$ok = array('success' => false, "error" => "no data");            
	try {		
			
		if ($_POST["key"] == $key) {
			if (isset($_POST["operation"])) {
				$op = $_POST["operation"];				
				$sala = getSala();
				if ($op == 0) {
					$sala->create();
				}elseif ($op == 1) {
					$sala->edite();
				}elseif ($op == 2) {										
					$sala->read();
				}elseif ($op == 3) {
					$sala->delete();
				}elseif($op == 4){
					$sala->readEndereco();
				}							
			}  
		}else{
			$ok = array('success' => false, "error" => "Not authorized!");        
		}			    
	} catch(PDOException $e) {	  	
	  	$ok = array('success' => false, "error" => $e->getMessage());        
	}  
	function getSala(){
		$sala = new Sala();				  		  		
		$sala->id_sala = isset($_POST["id"]) ? $_POST["id"]: null;
		$sala->nome = isset($_POST["nome"]) ? $_POST["nome"]: null;
		$sala->numero = isset($_POST["numero"]) ? $_POST["numero"]: null;
		$sala->disponivel = isset($_POST["disponivel"]) ? $_POST["disponivel"]: null;
		$sala->campus = isset($_POST["campus"]) ? $_POST["campus"]: null;		
		return $sala;
	}      	
?>