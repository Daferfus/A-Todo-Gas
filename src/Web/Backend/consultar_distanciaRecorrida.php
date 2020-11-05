<?php
include 'conexion.php';
//$idUsuario=$_POST['idUsuario'];
//$momento=$_POST['momento'];

$idUsuario="nachonachete@gmail.com";
$momento="hoy";
//Sentencia sql para consultar la distancia recorridos en 1 dia
$sentencia=$conexion->prepare("SELECT distancia FROM `distanciaypasosrecorridos` WHERE idUsuario=? AND momento=?");

//Comprobamos que la sentencia no sea false
if($sentencia===false){
     return [ 'ok' => 'false' ];
}
$sentencia->bind_param('ss',$idUsuario,$momento);
$sentencia->execute();

$resultado = $sentencia->get_result();
if ($fila = $resultado->fetch_assoc()) {
         echo json_encode($fila,JSON_UNESCAPED_UNICODE);     
}
$sentencia->close();
$conexion->close();
?>
