<?php
$hostname='localhost';
$database='Lecturas';
$username= 'root';
$password='';

$conexion=new mysqli($hostname,$username,$password,$database);
if($conexion->connect_errno){
    echo "El sitio web está experimentado problemas";
}else{
    //echo "Conexion realizada correctamente";
}
?>
