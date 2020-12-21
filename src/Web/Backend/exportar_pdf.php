<?php

 //use setasign\Fpdi\Fpdi;
 require_once('fpdf182/fpdf.php');
 require_once('FPDI-2.3.5/src/autoload.php');
 require_once('FPDI-2.3.5/src/Fpdi.php');
 include('conexion.php');
 include('functions.php');
class PDF extends \setasign\Fpdi\FPDI{
 /*function CrearTabla($header)
   {
     $pdf1 = new FPDF();
    //Cabecera
     $pdf1->SetFont('Arial','',10);
    foreach($header as $col){
    $pdf1->Cell(40,7,$col,1);
    //$pdf1->SetFont('','B');
    $pdf1->Ln();
    }*/
      
   

       

}
 // initiate FPDI
$pdf = new PDF();
 // add a page
$pdf->AddPage();
$pdf->SetFont('Arial','B',10);
 // set the sourcefile
$pdf->setSourceFile('plantillaSensores.pdf');
 // import page 1
$pagina1 = $pdf->importPage(1);
$pagina2 = $pdf->importPage(2);


// use the imported page and place it at point 10,10 with a width of 100 mm
$pdf->useTemplate($pagina1,null,null,null,null,true);
$pdf->AddPage();
$pdf->useTemplate($pagina2,null,null,null,null,true);


 //$header=array('idUsuario','Nombre','Apellidos','Ciudad','Sensor');
$pdf->AliasNbPages();
//$pdf->CrearTabla($header);
$pdf->Cell(0,50,'',0);
$pdf->Ln();
$pdf->Cell(35,5,'IdUsuario',1,'C');
$pdf->Cell(30,5,'Nombre',1,'C');
$pdf->Cell(30,5,'Apellidos',1,'C');
$pdf->Cell(30,5,'Ciudad',1,'C');
$pdf->Cell(30,5,'Sensor',1,'C');
$pdf->Ln();
 if($resultset=getSQLResultSet("SELECT usuarios.idUsuario, usuarios.nombre, usuarios.apellidos, usuarios.Ciudad, sensores.activo FROM `usuarios`,`sensores` WHERE sensores.activo= 0 AND usuarios.idUsuario=sensores.idUsuario")){

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
            $pdf->SetFont('Arial','',9);
            $pdf->Cell(35,5,$idUsuario,1,'C');
            $pdf->Cell(30,5,$nombre,1,'C');
            $pdf->Cell(30,5,$apellidos,1,'C');
            $pdf->Cell(30,5,$Ciudad,1,'C');
            $pdf->Cell(30,5,$textoActivo,1,'C');
            $pdf->Ln();
           
       
        }
    }


$pdf->Output('Sensores.pdf', 'D');
