<?php
include 'conexion.php';
include 'functions.php';

$idUsuario=$_GET['idUsuario'];
$idLogro=$_GET['idLogro'];

ejecutarComandoSQL("INSERT INTO `logrosconseguidosusuario`(`idUsuario`, `idLogro`) VALUES ('$idUsuario', '$idLogro')");

?>
