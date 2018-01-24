<?php  
	include 'conection.php';
	include 'model/Usuario.php';
	$ok = array('success' => false, "error" => "no data");            
	try {	
								
		$pdo = getConection();    
		$user = getUsuario();


		$data = isset($_POST["data"]) ? $_POST["data"]: null;
		if ($data != null) {
			$data = explode(",",$data);
			if ($data[2] ==  $key) {
				$user->id = $data[0];
				$user->editeProData($pdo,date("Y-m-d-h-m"),$data[1]);	 	   
			}
			
		}elseif ($_POST["key"] == $key) {
			if (isset($_POST["operation"])) {
				$op = $_POST["operation"];
				
				if ($op == 0) {
					$user->create($pdo);
				}elseif ($op == 1) {
					$user->edite($pdo);
				}elseif ($op == 2) {
					$where = $_POST["where"];
					$user->read($pdo,$where);
				}elseif ($op == 3) {
					$user->delete($pdo);
				}				
			}  
		}else{
			$ok = array('success' => false, "error" => "Not authorized!");        
		}			    
	} catch(PDOException $e) {	  	
	  	$ok = array('success' => false, "error" => $e->getMessage());        
	}  
	function getUsuario(){
		$user = new Usuario();				  		  		
		$user->id = isset($_POST["id"]) ? $_POST["id"]: null;
		$user->nome = isset($_POST["nome"]) ? $_POST["nome"]: null;
		$user->email = isset($_POST["email"]) ? $_POST["email"]: null;
		$user->telefone = isset($_POST["telefone"]) ? $_POST["telefone"]: null;
		$user->senha = isset($_POST["senha"]) ? $_POST["senha"]: null;
		$user->tipo = isset($_POST["prodata"]) ? $_POST["prodata"]: null;
		return $user;
	}      	
?>