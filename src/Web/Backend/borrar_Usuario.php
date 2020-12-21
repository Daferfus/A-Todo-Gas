<?php
include("conexion.php");
include("functions.php");

$idUsuario=$_POST['idUsuario'];
//$idUsuario="adrian@gmail.com";
//$idUsuario= ( empty($_POST['idUsuario']) ) ? NULL : $_POST['idUsuario'];
//echo $idUsuario;
ejecutarComandoSQL("DELETE FROM logrosconseguidosusuario WHERE idUsuario='$idUsuario'");
ejecutarComandoSQL("DELETE FROM medialecturas WHERE idUsuario='$idUsuario'");
ejecutarComandoSQL("DELETE FROM sensores WHERE idUsuario='$idUsuario'");
ejecutarComandoSQL("DELETE FROM usuarios WHERE idUsuario='$idUsuario'");
ejecutarComandoSQL("DELETE FROM distanciaypasosrecorridos WHERE idUsuario='$idUsuario'");
ejecutarComandoSQL("DELETE FROM lecturas WHERE idUsuario='$idUsuario'");

echo '<h2>Usuario borrado correctamente</h2>
    <h5>Usuario borrado correctamente</h5>
    <a href="../Frontend/usuarios.php"> Volver atrás</a>';
?>
