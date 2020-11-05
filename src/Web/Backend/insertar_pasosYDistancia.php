<?php
include 'conexion.php';
include 'functions.php';

$idUsuario=$_GET['idUsuario'];
$momento=$_GET['momento'];
$pasos=$_GET['pasos'];
$distancia=$_GET['distancia'];

ejecutarComandoSQL("INSERT INTO `distanciaypasosrecorridos`(`idUsuario`, `momento`, `pasos`, `distancia`) VALUES ('$idUsuario', '$momento', '$pasos', '$distancia')");

?>
