<?php	
	class Produto{
		public $id;
		public $id_loja;
	    public $nome;
    	public $descricao;
    	public $preco;    	
    	public $categoria;
    	public $estado;
    	public $estoque;
    	public $unidade;

     function create($pdo){		
		$stmt = $pdo->prepare("SELECT id_produto FROM produto where id_loja=$this->id_loja and nome = :nome;");		 		

		$stmt->execute(array(		    				
		    ':nome' => $this->nome
		));	   		
		if ($stmt->fetch()[0] == null) {
			$stmt = $pdo->prepare("INSERT INTO produto(nome,descricao,preco,estado,categoria,estoque,unidade,id_loja)
	  	 						VALUES (:nome,:descricao,:preco,:estado,:categoria,:estoque,:unidade,:id_loja)");			
	  		$stmt->execute(array(		    				
			    ':nome' => $this->nome,
			    ':descricao' => $this->descricao,
	    		':preco' => $this->preco,
	    		':estado' => $this->estado,	    		
	    		':categoria' => $this->categoria,				
				':estoque' => $this->estoque,	    		
	    		':unidade'=> $this->unidade,
	    		':id_loja' => $this->id_loja
			));	   			
	  		$ok = array('success' => true, 'id_produto' => $pdo->lastInsertId());            	  		   
		}else{
			$ok = array('success' => false, "error" => 0);		
		}				  			  		
		echo json_encode($ok); 
	}	
	function edite($pdo){		 	
    	$stmt = $pdo->prepare("SELECT id_produto FROM produto where id_loja=$this->id_loja and nome = :nome;");		 		

		$consulta = $stmt->execute(array(		    				
		    ':nome' => $this->nome,		    
		));	 
		if ($consulta->fetch()[0] == null) {			
			$stmt = $pdo->prepare('UPDATE usuario SET
	    		nome = :nome,
	    		descricao = :email,
	    		preco = :preco,
	    		estado = :estado,
	    		id_loja =:id_loja,
	    		categoria = :categoria,
	    		estoque = :estoque,
	    		unidade = :unidade,
	    		estoque = :estoque
	    		WHERE id_produto = :id');
			$stmt->execute(array(		    				
				':id' => $this->id,				
			    ':nome' => $this->nome,
			    ':descricao' => $this->descricao,
	    		':preco' => $this->preco,
	    		':estado' => $this->estado,
	    		':id_loja' => $this->id_loja,
	    		':categoria' => $this->categoria,
	    		':estoque'=> $this->estoque,
	    		':unidade'=> $this->unidade
			));	 
			//var_dump($stmt->errorInfo());
					
	  		$ok = array('success' => true);            	  		   
		}else{
			$ok = array('success' => false, "error" => 0);		
		}				  			  		
		echo json_encode($ok);     	
	}
	function read($pdo){		
		try {
			$limit = $_POST["limit"];
			$index = $_POST["index"];		
			$w = isset($_POST["id_loja"]) ? "where id_loja = ".$_POST["id_loja"]: null;
		
			$consulta = $pdo->query("SELECT * FROM produto $w LIMIT $limit OFFSET $index;");					
			$c = $consulta->fetchAll(PDO::FETCH_CLASS);					
			$json = array();			
			foreach ($c as $key => $value) {				
				array_push($json, array(
					'id_produto' => $value->id_produto,				
				    'nome' => $value->nome,
				    'descricao' => $value->descricao,
		    		'preco' => $value->preco,
		    		'estado' => $value->estado,
		    		'id_loja' => $value->id_loja,
		    		'categoria' => $value->categoria,
		    		'estoque'=> $value->estoque,
		    		'unidade'=> $value->unidade
				));							
			}		
						
			$ok = array('success' => true, 'produtos' =>$json);					
		} catch (Exception $e) {
			$ok = array('success' => false, "error" => $e->getMessage());        			
		}		
		echo json_encode($ok);               			
	}
	function delete($pdo){
		$id = $_POST["id"];
		try {			  			 
			$stmt = $pdo->prepare('DELETE FROM produto WHERE id_produto = :id');
		  
		  	$stmt->bindParam(':id', $this->id); 
		  	$stmt->execute();		  
		  	$ok = array('success' => true);
		} catch(PDOException $e) {
		  	$ok = array('success' => false, "error" => 0);
		}		
		echo json_encode($ok); 
	}
}	
 ?>