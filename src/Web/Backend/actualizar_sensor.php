<?php
header('Access-Control-Allow-Origin: *');
header("Access-Control-Allow-Headers: X-API-KEY, Origin, X-Requested-With, Content-Type, Accept, Access-Control-Request-Method");
header("Access-Control-Allow-Methods: GET, POST, OPTIONS, PUT, DELETE");
header("Allow: GET, POST, OPTIONS, PUT, DELETE");

include 'conexion.php';
//comprobar que el metodo es POST
if($_SERVER['REQUEST_METHOD'] == 'POST'){
    //declaracion de las variables a subir
$idUsuario;

if (isset($_POST['idUsuario']))
{
    $idUsuario = $_POST['idUsuario'];
}
$estado;

if (isset($_POST['estado']))
{
    $estado = $_POST['estado'];
}


    //consulta sql para introducir los valores
$consulta="UPDATE sensores SET activo ='$estado' WHERE idUsuario='$idUsuario'";
mysqli_query($conexion, $consulta) or die (mysqli_error());
mysqli_close($conexion);
}
?>