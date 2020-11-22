<?php
include 'conexion.php';
//$idUsuario=$_POST['idUsuario'];

$idUsuario="nachonachete@gmail.com";

//Sentencia sql para consultar la distancia recorridos en 1 dia
$sentencia=$conexion->prepare("SELECT NombreLogro FROM `logros`, `logrosconseguidosusuario` WHERE idUsuario=?");

//Comprobamos que la sentencia no sea false
if($sentencia===false){
     return [ 'ok' => 'false' ];
}
$sentencia->bind_param('s',$idUsuario);
$sentencia->execute();

$resultado = $sentencia->get_result();
if ($fila = $resultado->fetch_assoc()) {
         echo json_encode($fila,JSON_UNESCAPED_UNICODE);     
}
$sentencia->close();
$conexion->close();
?>
