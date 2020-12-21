<?php
include('conexion.php');
include('functions.php');

if($resultset=getSQLResultSet("SELECT usuarios.idUsuario, usuarios.nombre, usuarios.apellidos, usuarios.Ciudad, sensores.activo FROM `usuarios`,`sensores` WHERE sensores.activo= 1 AND usuarios.idUsuario=sensores.idUsuario")){

    while($row = $resultset->fetch_array()){
        //echo json_encode($row);         
        $idUsuario= $row['idUsuario'];
       
        $nombre= $row['nombre'];
        $apellidos= $row['apellidos'];
        
        $Ciudad= $row['Ciudad'];
        $activo= $row['activo'];
        $textoActivo='Inactivo';
        if(!$activo==1){
            $textoActivo='Inactivo';
        }else{
            $textoActivo='Activo';
        } 
        
        
       
    ?>
<tr>
    <td><?php echo $idUsuario;?></td>
    <td><?php echo $nombre;?></td>
    <td><?php echo $apellidos;?></td>
    <td><?php echo $textoActivo;?></td>
    <td><?php echo $Ciudad;?></td>
    <td>
        <form class="iconos-tabla-usuarios">

            <a href="#" data-toggle="modal" data-target="#eliminarUsuariotModal" id="boton-eliminar" name="<?php echo $idUsuario?>">
                <!-- Llama al HTML que se encuentra al final de este archivo, muestra pop-up-->
                <i class="fas fa-trash"></i>
            </a>
            <i class="fas fa-pen"></i>
        </form>
    </td>

</tr>

<?php
    

    }
}

?>
