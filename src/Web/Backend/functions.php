// ---------Autor: Adrian Vaello---------
// ---------20/10/2020---------

<?php
header('Content-Type: text/html;charset=utf-8' );

function ejecutarComandoSQL($commando){
    $mysqli = new mysqli("localhost", "root", "", "Lecturas");
    if($mysqli->connect_errno){
        printf("Connect failed: %s\n", $mysqli->connect_error);
        exit();
    }
    
    if($mysqli->multi_query($commando)){
        if($resultset = $mysqli->store_result()){
            while($row = $resultset->fetch_array(MYSQLI_BOTH)){
                
            }
            $resultset->free();
        }
    }
    
    $mysqli->close();
}

function getSQLResultSet($commando){
    $mysqli = new mysqli("localhost", "root", "", "Lecturas");
    if($mysqli->connect_errno){
        printf("Connect failed: %s\n", $mysqli->connect_error);
        exit();
    }
    if($mysqli->multi_query($commando)){
        return $mysqli->store_result();
    }
}
