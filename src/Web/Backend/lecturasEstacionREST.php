<?php
// ----------------------------------------------------------
// David Fernández Fuster
// 04-09-20
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

// ------------------------------------------------------------------
//                              POST
// ------------------------------------------------------------------
// Si hay una petición POST, sigue adelante...
if($_SERVER['REQUEST_METHOD']=='POST'){
 
 // ...recoge los parametros enviados a través del cuerpo de la petición...
 $dia = $_POST['dia'];
 $hora = $_POST['hora'];
 $ubicacion = $_POST['ubicacion'];
 $valor = $_POST['valor']; 
 $idMagnitud = $_POST['idMagnitud']; 
 // ...crea una sentencia para insertar dichos parámetros en la base de datos...
 $stmt = $conn->prepare("INSERT INTO lecturasEstacion VALUES (?, ?, ?, ?, ?)");
 
 // ...ata los parámetros a dicha sentencia...
 $stmt->bind_param("sssss",$dia,$hora,$ubicacion,$valor,$idMagnitud);
 
 // ...y finalmente ejecuta dicha sentencia. Y en caso de ser un éxito...
 if($stmt->execute()){
  // ...envía una respuesta HTTP 202 OK. 
  $response['error'] = false; 
  $response['message'] = 'Lectura guardada con éxito'; 
 }else{
  // En caso contrario da un mensaje de error.
  $response['error'] = true; 
  $response['message'] = 'Por favor, inténtalo de nueov';
 } // if()
} // if()
// ------------------------------------------------------------------
// ------------------------------------------------------------------


// ------------------------------------------------------------------
//                            GET
// ------------------------------------------------------------------
// En cambio, si hay una petición GET...
else if($_SERVER['REQUEST_METHOD']=='GET'){
 
 $lectura = 0;

 $dia = $_GET['dia'];
 $hora = $_GET['hora'];
 $ubicacion = $_GET['ubicacion'];
 // ...crea una consulta SQL para recoger todas las lecturas...
 $stmt = "SELECT valor FROM lecturasEstacion WHERE dia='$dia' AND hora='$hora' AND ubicacion='$ubicacion'";

 // ...se ejecuta la consulta...
 $retval = mysqli_query($conn, $stmt);
 
 // ...y si esta no ha sido un éxito...
 if(! $retval ) {
    // ...se mata al proceso y se manda un mensaje de error...
    die('No se han podido recoger las lecturas: ' . mysqli_error());
 } // if()

 //
 //
 //
 // En caso contrario, y de haber algún resultado...
 if (mysqli_num_rows($retval) > 0) {
   // ...y por cada fila de la tabla recogida...
   while($row = mysqli_fetch_assoc($retval)) {
      // ...se asocian los datos a sus respectivas variables...
      $valor = $row['valor'];
      
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
