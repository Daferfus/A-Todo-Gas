<?php
include 'conexion.php';
if($_SERVER['REQUEST_METHOD'] == 'GET'){
$idUsuario;
if (isset($_GET['idUsuario']))
{
    $idUsuario = $_GET['idUsuario'];
}
$dia;
if (isset($_GET['dia']))
{
    $dia = $_GET['dia'];
}

//Sentencia sql para consultar los pasos recorridos en 1 dia
$sentencia=$conexion->prepare("SELECT distancia FROM `distanciaypasosrecorridos` WHERE idUsuario='$idUsuario' AND dia='$dia'");

//Comprobamos que la sentencia no sea false
if($sentencia===false){
     return [ 'ok' => 'false' ];
}

$sentencia->execute();

$resultado = $sentencia->get_result();
if ($fila = $resultado->fetch_assoc()) {
         echo json_encode($fila,JSON_UNESCAPED_UNICODE);     
}
$sentencia->close();
$conexion->close();
}
?>
