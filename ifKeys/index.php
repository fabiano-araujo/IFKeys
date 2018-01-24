
<!doctype html>
<html lang="en">
  <head>
    <!-- Required meta tags -->
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">

    <!-- Bootstrap CSS -->

    <link rel="stylesheet" href="css/bootstrap.min.css">
  
    <title>Hello, world!</title>

    <script src="js/jquery-3.2.1.min.js"></script>
    <script src="js/bootstrap.min.js"></script>
    
  
    <style>
      .padding{
        padding: 0;
        padding-left: 7px;
        padding-right: 7px;
        margin-top: 30px;
      }
      .tab{
          color: #0098F0;
      }
    </style>

    <script>
      //window.history.pushState("", "", '/snapdark');
      $("button").click(function(){
          $("#div1").load("demo_test.txt", function(responseTxt, statusTxt, xhr){
              if(statusTxt == "success")
                  alert("External content loaded successfully!");
              if(statusTxt == "error")  
                  alert("Error: " + xhr.status + ": " + xhr.statusText);                
          });
      });
    </script>
  </head>
  <body>
      <div id="dz-root"></div>
      <script src="https://files.deezer.com/js/min/dz.js"></script>
      <script>
        DZ.init({
          appId  : '233062',
          channelUrl : 'http://snapdark.com/channel.html'
        });
       
      </script>
      <script src="js/script.js"></script>


      <nav class="navbar navbar-expand-lg navbar-dark bg-dark">
        <a class="navbar-brand" href="#">Snapdark</a>
        <button class="navbar-toggler" type="button" data-toggle="collapse" data-target="#navbarSupportedContent" aria-controls="navbarSupportedContent" aria-expanded="false" aria-label="Toggle navigation">
          <span class="navbar-toggler-icon"></span>
        </button>

        <div class="collapse navbar-collapse" id="navbarSupportedContent">
          <ul class="navbar-nav mr-auto">
            <li class="nav-item active">
              <a class="nav-link" href="#">Home <span class="sr-only">(current)</span></a>
            </li>
            <li class="nav-item">
              <a class="nav-link" href="#">Link</a>
            </li>                    
          </ul>  

          <form class="form-inline my-2 my-lg-0">
              <input class="form-control mr-sm-2" type="search" placeholder="Search" aria-label="Search">
              <button class="btn btn-outline-success my-2 my-sm-0" type="submit">Search</button>
            </form>        
        </div>
      </nav>

      <ul class="nav justify-content-center">
        <li class="nav-item tab">
          <a class="nav-link">Recomendado</a>
        </li>
        <li class="nav-item tab">
          <a class="nav-link">Destaque</a>
        </li>
        <li class="nav-item tab">
          <a class="nav-link">Favoritos</a>
        </li>        
      </ul>
      <main id="main">       
        <div class="container">     
          <div class="row">
          
            <div class="col-sm-2 padding">
              <div class="card">
                <img class="card-img-top" src="http://www.topimagens.com.br/imagens/imagens-mais-novas.jpg" alt="Card image cap">                        
              </div>
              <center><h5>Titulo</h5></center>
            </div>         


              <div class="col-sm-2 padding">
              <div class="card">
                <img class="card-img-top" src="http://www.topimagens.com.br/imagens/imagens-mais-novas.jpg" alt="Card image cap">                        
              </div>
              <center><h5>Titulo</h5></center>
            </div>                    

              <div class="col-sm-2 padding">
              <div class="card">
                <img class="card-img-top" src="http://www.topimagens.com.br/imagens/imagens-mais-novas.jpg" alt="Card image cap">                        
              </div>
              <center><h5>Titulo</h5></center>
            </div>                    
              <div class="col-sm-2 padding">
              <div class="card">
                <img class="card-img-top" src="http://www.topimagens.com.br/imagens/imagens-mais-novas.jpg" alt="Card image cap">                        
              </div>
              <center><h5>Titulo</h5></center>
            </div>                    

              <div class="col-sm-2 padding">
              <div class="card">
                <img class="card-img-top" src="http://www.topimagens.com.br/imagens/imagens-mais-novas.jpg" alt="Card image cap">                        
              </div>
              <center><h5>Titulo</h5></center>
            </div>                    


              <div class="col-sm-2 padding">
              <div class="card">
                <img class="card-img-top" src="http://www.topimagens.com.br/imagens/imagens-mais-novas.jpg" alt="Card image cap">                        
              </div>
              <center><h5>Titulo</h5></center>
            </div>                    


              <div class="col-sm-2 padding">
              <div class="card">
                <img class="card-img-top" src="http://www.topimagens.com.br/imagens/imagens-mais-novas.jpg" alt="Card image cap">                        
              </div>
              <center><h5>Titulo</h5></center>
            </div>                    


              <div class="col-sm-2 padding">
              <div class="card">
                <img class="card-img-top" src="http://www.topimagens.com.br/imagens/imagens-mais-novas.jpg" alt="Card image cap">                        
              </div>
              <center><h5>Titulo</h5></center>
            </div>                               
          </div>           
        </div>
      </main>
  </body>
</html>