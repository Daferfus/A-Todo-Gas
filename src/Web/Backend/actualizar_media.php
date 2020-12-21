<?php
header('Access-Control-Allow-Origin: *');
header("Access-Control-Allow-Headers: X-API-KEY, Origin, X-Requested-With, Content-Type, Accept, Access-Control-Request-Method");
header("Access-Control-Allow-Methods: GET, POST, OPTIONS, PUT, DELETE");
header("Allow: GET, POST, OPTIONS, PUT, DELETE");

include 'conexion.php';
//comprobar que el metodo es POST
if($_SERVER['REQUEST_METHOD'] == 'POST'){
    //declaracion de las variables a subir
$dia;
//en caso de recibir un valor para la variable en cuestion lo toma
if (isset($_POST['dia']))
{
    $dia = $_POST['dia'];
}
    $hora;

if (isset($_POST['hora']))
{
    $hora = $_POST['hora'];
}
$idUsuario;

if (isset($_POST['idUsuario']))
{
    $idUsuario = $_POST['idUsuario'];
}
$valor;

if (isset($_POST['valor']))
{
    $valor = $_POST['valor'];
}
    //consulta sql para introducir los valores
$consulta="UPDATE medialecturas SET valor ='$valor' WHERE idUsuario='$idUsuario' AND dia='$dia'AND hora='$hora'";
mysqli_query($conexion, $consulta) or die (mysqli_error());
mysqli_close($conexion);
}
?>