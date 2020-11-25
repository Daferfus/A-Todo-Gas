<?php
include 'conexion.php';
$idUsuario=$_POST['idUsuario'];
$contrasenya=$_POST['contrasenya'];

//$idUsuario="nachonachete@gmail.com";
//$contrasenya="123456789";

$sentencia=$conexion->prepare("SELECT * FROM `usuarios` WHERE idUsuario=? AND contrasenya=?");

//Comprobamos que la sentencia no sea false
if($sentencia===false){
     return [ 'ok' => 'false' ];
}
$sentencia->bind_param('ss',$idUsuario,$contrasenya);
$sentencia->execute();

$resultado = $sentencia->get_result();
if ($fila = $resultado->fetch_assoc()) {
         echo json_encode($fila,JSON_UNESCAPED_UNICODE);     
}
$sentencia->close();
$conexion->close();
?>
