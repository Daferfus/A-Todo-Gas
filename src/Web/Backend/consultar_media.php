<?php
include 'conexion.php';
//$idUsuario=$_POST['idUsuario'];
//$momento=$_POST['momento'];
if($_SERVER['REQUEST_METHOD'] == 'GET'){
$dia;
if (isset($_GET['dia']))
{
    $dia = $_GET['dia'];
}
    $hora;

if (isset($_GET['hora']))
{
    $hora = $_GET['hora'];
}
$idUsuario;
if (isset($_GET['idUsuario']))
{
    $idUsuario = $_GET['idUsuario'];
}
//Sentencia sql para consultar los pasos recorridos en 1 dia
$sentencia=$conexion->prepare("SELECT valor FROM `medialecturas` WHERE dia='$dia'AND hora='$hora' AND idUsuario='$idUsuario'");

//Comprobamos que la sentencia no sea false
if($sentencia===false){
     return [ 'ok' => 'false' ];
}
//$sentencia->bind_param('s',$idUsuario);
$sentencia->execute();

$resultado = $sentencia->get_result();
if ($fila = $resultado->fetch_assoc()) {
         echo json_encode($fila,JSON_UNESCAPED_UNICODE);     
}
$sentencia->close();
$conexion->close();
}
?>