<?php 
// Get current working directory
// magicSquare.m exists in this directory

//El arxivo .m debe estar en la misma carpeta que este php
$pwd = getcwd();
echo $pwd;
//$versionMatlab='R2018a';
// Set command. Please use -r option and remember to add exit in the last 

//Hay que cambiar la version de matlab por la vuestra
//Cambiar "magicSquare" por el nombre de la funcion interpolación
$cmd = 'C:\Program Files\MATLAB\R2018a\bin\matlab -automation -sd ' . $pwd . ' -r "magicSquare(5);exit" -wait -logfile log.txt';
// exec
$last_line = exec($cmd, $output, $retval);
//echo $retval;
if ($retval == 1 || $retval== 0){
  // Read from CSV file which MATLAB has created
    
  //Archivo con los valores devueltos de matlab
  $lines = file('result.csv');
  echo '<p>Results:<br>';
  foreach($lines as $line)
  {
    echo $line.'<br>';
  }
  echo '</p>';
} else {
  // When command failed
  echo '<p>Failed</p>';
}
?>
