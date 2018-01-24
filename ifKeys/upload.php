<?php     
    $ok = array('success' => false, "error" => "no data");    
    if (isset($_POST["encodedImage"])) {
        $base = $_POST["encodedImage"];
        $user = $_POST["user"];
        $type = isset($_POST["type"]) ? $_POST["type"]: null;
        $lojaId = isset($_POST["lojaId"])?$_POST["lojaId"]: null;
        try {            
            $url = "http://localhost/ecomerce/uploads/";
            
            $path = null; 
            if ($lojaId == null) {
                $image_name = "img_"."_".date("Y-m-d-H-m-s").".jpg";
                $path = "uploads/user".$user ."/".$type."/";   
            }else{
                $image_name = "logo.jpg";
                $path = "uploads/user".$user ."/loja".$lojaId."/";   
            }            

            // base64 encoded utf-8 string
            $binary = base64_decode($base);

            // binary, utf-8 bytes
            header("Content-Type: bitmap; charset=utf-8");

            if (!file_exists($path)) {
                mkdir($path, 0777, true);
            }
            $file = fopen($path. $image_name, "wb"); // 
            $filepath = $image_name; 
            fwrite($file, $binary);

            fclose($file);  
            $ok = array('success' => true);            
        } catch (Exception $e) {
            $ok = array('success' => false, "error" => $e->getMessage());           
        }        
    }
    echo json_encode($ok);               
 ?>