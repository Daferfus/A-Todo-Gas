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
$dia;
//en caso de recibir un valor para la variable en cuestion lo toma
if (isset($_POST['dia']))
{
    $dia = $_POST['dia'];
}

$distancia;

if (isset($_POST['distancia']))
{
    $distancia = $_POST['distancia'];
}

$pasos;

if (isset($_POST['pasos']))
{
    $pasos = $_POST['pasos'];
}
    //consulta sql para introducir los valores
$consulta="UPDATE distanciaypasosrecorridos SET distancia ='$distancia', pasos='$pasos' WHERE idUsuario='$idUsuario' AND dia='$dia'";
mysqli_query($conexion, $consulta) or die (mysqli_error());
mysqli_close($conexion);
}
?>