<?php
include("conexion.php");
include("functions.php");

$idUsuario=$_POST['idUsuario'];
$nombre=$_POST['nombre'];
$apellidos=$_POST['apellidos'];
$Contrasenya=$_POST['Contrasenya'];
$ciudad=$_POST['ciudad'];

//$idUsuario="adrian@gmail.com";
//$nombre="adrian";
//$apellidos="gmail.com";
//$Contrasenya="adrian@gmail.com";
//$Ciudad="La vila";

//ejecutarComandoSQL("INSERT INTO `distanciaypasosrecorridos`(`idUsuario`, `momento`, `pasos`, `distancia`) VALUES ('$idUsuario', '$nombre', '$apellidos', '$Contrasenya', '$Ciudad', )");

ejecutarComandoSQL("INSERT INTO `usuarios` VALUES ('$idUsuario', '$nombre', '$apellidos', '$Contrasenya',0, '$ciudad')");

ejecutarComandoSQL("INSERT INTO `sensores`(`idUsuario`, `activo`) VALUES ('$idUsuario', 1)");

echo '<h2>Registro completo</h2>
    <h5>Usuario registrado correctamente</h5>
    <a href="../Frontend/usuarios.php"> Volver atrás</a>';
?>
