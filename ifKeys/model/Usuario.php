<?php
class Usuario{
	public $id;
    public $nome;
	public $email;
	public $telefone;
	public $senha;    	
	public $prodata;
	

    function create($pdo){		
		$consulta = $pdo->query("SELECT email FROM usuario where email='$this->email';");		 		
		if ($consulta->fetch()[0] == null) {
			$stmt = $pdo->prepare("INSERT INTO usuario(nome,telefone,email,tipo,senha)
	  	 		VALUES (:nome,:telefone,:email,:prodata,:senha)");
	  		$stmt->execute(array(		    				
			    ':nome' => $this->nome,
			    ':email' => $this->email,
	    		':telefone' => $this->telefone,
	    		':senha' => $this->senha,
	    		':prodata'=> $this->prodata
			));	   
	  		$ok = array('success' => true, 'id_user' => $pdo->lastInsertId());            	  		   
		}else{
			$ok = array('success' => false, "error" => 0);		
		}				  			  		
		echo json_encode($ok);               
	}	
	function edite($pdo){		 	
    	$consulta = $pdo->query("SELECT email FROM usuario where email='$this->email';");		 		    	
		if ($consulta->fetch()[0] == null) {			
			$stmt = $pdo->prepare('UPDATE usuario SET
	    		nome = :nome,
	    		email = :email,
	    		telefone = :telefone,
	    		senha = :senha,	    		
	    		WHERE id_usuario = :id');
			$stmt->execute(array(		    
				':id' => $this->id,
			    ':nome' => $this->nome,
			    ':email' => $this->email,
	    		':telefone' => $this->telefone,
	    		':senha' => $this->senha,	    		
			));		 
			//var_dump($stmt->errorInfo());
					
	  		$ok = array('success' => true);            	  		   
		}else{
			$ok = array('success' => false, "error" => 0);		
		}				  			  		
		echo json_encode($ok); 
    	
	}

	function editeProData($pdo,$data,$qtdDays){		 	    		
		$stmt = $pdo->prepare('UPDATE usuario SET    		
    		prodata = :prodata    		
    		WHERE id_usuario = :id');
		$stmt->execute(array(	
			':id' => $this->id,
    		':prodata'=> $date.",".$qtdDays
		));		 		
		//date("Y-m-d-h-m")
		//var_dump($stmt->errorInfo());
				
  		$ok = array('success' => true);            	  		   						  			  	
		echo json_encode($ok); 
    	
	}

	function read($pdo,$where){		
		try {
			if ($where == null) {
				$limit = $_POST["limit"];
				$index = $_POST["index"];	
				$consulta = $pdo->query("SELECT * FROM usuario LIMIT $limit OFFSET $index;");		
			}else{
				$consulta = $pdo->prepare("SELECT id_usuario,nome,telefone,email,prodata FROM usuario where email = :email and senha = :senha;");		

				$consulta->execute(array(		    					
				    ':email' => $this->email,		    		
		    		':senha' => $this->senha	    		
				));	
			}
			$data = explode(",",$consulta['prodata'])

			$now = time(); // or your date as well			
			$your_date = strtotime($data[0]);
			$datediff = $now - $your_date;

			$diasRestantes = floor($datediff / (60 * 60 * 24));

			$consulta['prodata'] = $consulta['prodata'].",$diasRestantes";

			$c = $consulta->fetchAll(PDO::FETCH_CLASS );		
			$ok = array('success' => true, 'usuarios' =>$c);					
		} catch (Exception $e) {
			$ok = array('success' => false, "error" => $e->getMessage());        			
		}		
		echo json_encode($ok);               			
	}
	function delete($pdo){
		$id = $_POST["id"];
		try {			  			 
			$stmt = $pdo->prepare('DELETE FROM loja WHERE id_usuario = :id');

		  	$stmt2 = $pdo->prepare('DELETE FROM usuario WHERE id_usuario = :id');
		  	$stmt->bindParam(':id', $this->id); 
		  	$stmt->execute();

		  	$stmt2->bindParam(':id', $this->id); 
		  	$stmt2->execute();		  	
		  	$ok = array('success' => true);
		} catch(PDOException $e) {
		  	$ok = array('success' => false, "error" => 0);
		}		
		echo json_encode($ok); 
	}
}
?>