<?php
// ----------------------------------------------------------
// David Fernández Fuster
// 12-11-20
// ----------------------------------------------------------

// --------------------------------------------------------------
// Constantes de la Base de Datos
// --------------------------------------------------------------
define('DB_HOST','localhost');
define('DB_USERNAME','root');
define('DB_PASSWORD','');
define('DB_NAME', 'Lecturas');
 

// --------------------------------------------------------------
// Conexión
// --------------------------------------------------------------
// Conectamos con la base de datos...
$conn = new mysqli(DB_HOST, DB_USERNAME, DB_PASSWORD, DB_NAME);
 
// ...y comprobamos si la conexión ha sido un éxito.
if($conn->connect_error) {
    die("Connection failed: " . $conn->connect_error);
} // if()
 

// --------------------------------------------------------------
// Peticiones REST
// --------------------------------------------------------------
// Creamos un array para almacenar la respuesta HTTP de las peticiones REST.
$response = array(); 

if($_SERVER['REQUEST_METHOD']=='GET'){
 
    $desviacion = 0;
   
    $idUsuario=$_GET['idUsuario'];

    //Sentencia sql para consultar la distancia recorridos en 1 dia
    $stmt = "SELECT desviacion FROM `usuarios` WHERE idUsuario='$idUsuario'";
    
    // ...se ejecuta la consulta...
    $retval = mysqli_query($conn, $stmt);
    
    // ...y si esta no ha sido un éxito...
    if(! $retval ) {
       // ...se mata al proceso y se manda un mensaje de error...
       die('No se ha podido recoger la desviación' . mysqli_error());
    } // if()
   
    //
    //
    //
    // En caso contrario, y de haber algún resultado...
    if (mysqli_num_rows($retval) > 0) {
      // ...y por cada fila de la tabla recogida...
      while($row = mysqli_fetch_assoc($retval)) {
         // ...se asocian los datos a sus respectivas variables...
         $valor = $row['desviacion'];
         
         // ...y cada variable es asignada a una propiedad y puesta en un array...
         $lectura = $valor; 
      } // while()
      //
      //
      //
      // ...para luego ser codificada en formato JSON y enviada como respuesta a la petición HTTP.
      echo $lectura;
    } else {
       // ...de lo contrario, se deja constancia de ello en la respuesta...
      echo "0 resultados";
    } // if()
    // ...y se cierra conexión.
    mysqli_close($conn); 
   
    // De lo contrario devuelve mensaje de error.
   }else{
    $response['error'] = true; 
    $response['message'] = "Invalid request"; 
   } // if()
   // ------------------------------------------------------------------
   // ------------------------------------------------------------------
   
   // La respuesta a las petición HTTP se devuelve en formato JSON. 
   echo json_encode($response);
?> 
