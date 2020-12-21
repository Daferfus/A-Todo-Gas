<?php

// ----------------------------------------------------------
// David Fernández Fuster
// 04-09-20
// ----------------------------------------------------------

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
$desviacion;
//en caso de recibir un valor para la variable en cuestion lo toma
if (isset($_POST['desviacion']))
{
    $desviacion = $_POST['desviacion'];
}

//consulta sql para introducir los valores
$consulta= "UPDATE usuarios SET desviacion='$desviacion' WHERE idUsuario='$idUsuario'";
mysqli_query($conexion, $consulta) or die (mysqli_error());
mysqli_close($conexion);
}
?>