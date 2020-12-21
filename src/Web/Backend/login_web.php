<?php
include 'conexion.php';

$idUsuario=$_POST['idUsuario'];
$contrasenya=$_POST['contrasenya'];

//$idUsuario="nachonachete@gmail.com";
//$contrasenya="123456789";

$query = mysqli_query($conexion,"SELECT * FROM `usuarios` WHERE idUsuario='".$idUsuario."' AND contrasenya='".$contrasenya."'");
$nr= mysqli_num_rows($query);
echo $nr;
if($nr==1){
    header("Location: ../Frontend/admin_page.php");
}else{
     //$msg = "El nombre de usuario o la contraseña no son correctos.";
    header('Location: ../Frontend/index.html');
    //echo "<span class='warning_msg'>".$_GET["msg"]."</span>";
}
?>
