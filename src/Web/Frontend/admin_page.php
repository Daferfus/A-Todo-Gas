<!DOCTYPE html>
<html lang="en">

<head>

    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
    <meta name="description" content="">
    <meta name="author" content="">

    <title>A Tod Gas - Panel</title>

    <!-- Bootstrap core CSS-->
    <link href="vendor/bootstrap/css/bootstrap.min.css" rel="stylesheet">

    <!-- Custom fonts for this template-->
    <link href="vendor/fontawesome-free/css/all.min.css" rel="stylesheet" type="text/css">

    <!-- Page level plugin CSS-->
    <link href="vendor/datatables/dataTables.bootstrap4.css" rel="stylesheet">

    <!-- Custom styles for this template-->
    <link href="css/sb-admin.css" rel="stylesheet">


    <!-- Switch -->
    <script src="js/switch.js"></script>

</head>

<body id="page-top">
    <?php
    if(!empty($_POST['inlineRadioOptions'])){
    $datos=$_POST['inlineRadioOptions'];
    }else{
        $datos="option1";
    }
     ?>

    <!-- Barra superior header -->
    <nav class="navbar navbar-expand navbar-dark bg-dark static-top">

        <a class="navbar-brand mr-1" href="admin_page.html"><img src="img/logo.png" alt=""></a>

        <button class="btn btn-link btn-sm text-white order-1 order-sm-0" id="sidebarToggle" href="#">
            <i class="fas fa-bars"></i>
        </button>

    </nav>

    <div id="wrapper">

        <!-- Menú Lateral -->
        <ul class="sidebar navbar-nav">
            <li class="nav-item active">
                <a class="nav-link" href="admin_page.php">
                    <i class="fas fa-fw fa-tachometer-alt"></i>
                    <span>Panel</span>
                </a>
            </li>
            <li class="nav-item">
                <a class="nav-link" href="sensores.php">
                    <i class="fas fa-fw fa-chart-area"></i>
                    <span>Sensores</span></a>
            </li>
            <li class="nav-item">
                <a class="nav-link" href="usuarios.php">
                    <i class="fas fa-fw fa-users"></i>
                    <span>Usuarios</span></a>
            </li>

            <li class="nav-item">
                <a class="nav-link" href="#" data-toggle="modal" data-target="#logoutModal">
                    <!-- Llama al HTML que se encuentra al final de este archivo, muestra pop-up-->
                    <i class="fas fa-fw fa-sign-out-alt"></i>
                    <span>Cerrar Sesion</span></a>
            </li>
        </ul>


        <!-- Contenido Panel Principal -->
        <div id="content-wrapper">

            <div class="container-fluid">

                <!-- CardViews del Panel Inicial-->
                <div class="row">
                    <div class="col-xl-3 col-sm-6 mb-3">
                        <div class="card text-white bg-primary o-hidden h-100">
                            <div class="card-body">
                                <div class="card-body-icon">
                                    <i class="fas fa-fw fa-comments"></i>
                                </div>
                                <div class="mr-5">Envía correos a clientes</div>
                            </div>
                            <a class="card-footer text-white clearfix small z-1" href="#">
                                <span class="float-left">Enviar</span>
                                <span class="float-right">
                                    <i class="fas fa-angle-right"></i>
                                </span>
                            </a>
                        </div>
                    </div>
                    <div class="col-xl-3 col-sm-6 mb-3">
                        <div class="card text-white bg-warning o-hidden h-100">
                            <div class="card-body">
                                <div class="card-body-icon">
                                    <i class="fas fa-fw fa-list"></i>
                                </div>
                                <div class="mr-5">Añadir Usuarios</div>
                            </div>
                            <a class="card-footer text-white clearfix small z-1" href="usuarios.php">
                                <span class="float-left">Añadir</span>
                                <span class="float-right">
                                    <i class="fas fa-angle-right"></i>
                                </span>
                            </a>
                        </div>
                    </div>
                    <div class="col-xl-3 col-sm-6 mb-3">
                        <div class="card text-white bg-success o-hidden h-100">
                            <div class="card-body">
                                <div class="card-body-icon">
                                    <i class="fas fa-fw fa-pen"></i>
                                </div>
                                <div class="mr-5">Añadir publicaciones</div>
                            </div>
                            <a class="card-footer text-white clearfix small z-1" href="#">
                                <span class="float-left">Añadir</span>
                                <span class="float-right">
                                    <i class="fas fa-angle-right"></i>
                                </span>
                            </a>
                        </div>
                    </div>
                    <div class="col-xl-3 col-sm-6 mb-3">
                        <div class="card text-white bg-danger o-hidden h-100">
                            <div class="card-body">
                                <div class="card-body-icon">
                                    <i class="fas fa-fw fa-life-ring"></i>
                                </div>
                                <div class="mr-5"><span>3</span> Sensores inactivos</div>
                            </div>
                            <a class="card-footer text-white clearfix small z-1" href="sensores.php">
                                <span class="float-left">Ver Sensores</span>
                                <span class="float-right">
                                    <i class="fas fa-angle-right"></i>
                                </span>
                            </a>
                        </div>
                    </div>
                </div>

                <!-- Ejemplo Gráfica1-->
                <div class="card mb-3">
                    <div class="card-header">
                        <i class="fas fa-chart-area"></i>
                        Tiempo uso sensores</div>
                    <div class="card-body">
                        <canvas id="myAreaChart" width="100%" height="30"></canvas>
                    </div>
                    <div class="card-footer small text-muted">Actualizada ayer a las 11:59 PM</div>
                </div>

                <!-- Ejemplo Tabla1 -->
                <div class="card mb-3">
                    <div class="card-header">
                        <i class="fas fa-table"></i>
                        Usuarios</div>
                    <div class="card-body">

                        <!-- Switch Redondo -->
                        <div class="switchContainer" id="radioButtons">
                            <div class="formSwitch">
                                <form class="form-check form-check-inline" method="post">
                                    <input class="form-check-input" type="radio" name="inlineRadioOptions" id="inlineRadio1" value="option1" <?php if($datos=="option1") echo "checked" ?> onchange="this.form.submit()">
                                    <label class="form-check-label" for="inlineRadio1">Mostrar todos</label>
                                </form>
                                <form class="form-check form-check-inline" method="post">
                                    <input class="form-check-input" type="radio" name="inlineRadioOptions" id="inlineRadio2" value="option2" onchange="this.form.submit()" <?php if($datos=="option2") echo "checked" ?>>
                                    <label class="form-check-label" for="inlineRadio2">Activos</label>
                                </form>
                                <form class="form-check form-check-inline" method="post">
                                    <input class="form-check-input" type="radio" name="inlineRadioOptions" id="inlineRadio3" value="option3" onchange="this.form.submit()" <?php if($datos=="option3") echo "checked" ?>>
                                    <label class="form-check-label" for="inlineRadio3">Inactivos</label>
                                </form>

                            </div>



                            <!--<label for="">Inactivo</label>

                            <label class="switch">
                                <input type="checkbox">
                                <span class="slider round"></span>
                            </label>
                            <label for="">Activos</label>-->
                            <div class="formPdf">
                                <form method="post" action="../Backend/exportar_pdf.php">
                                    <input type="image" src="img/pdf.png" width="35px" height="35px">

                                </form>
                            </div>

                        </div>

                        <div class="table-responsive">
                            <table class="table table-bordered" id="dataTable" width="100%" cellspacing="0">
                                <thead>
                                    <tr>
                                        <th>Usuario</th>
                                        <th>Nombre</th>
                                        <th>Apellido/s</th>
                                        <th>Sensor activo</th>
                                        <th>Ciudad</th>
                                        <th>Editar</th>
                                    </tr>
                                </thead>

                                <tbody>
                                    <?php
                                        if($datos=="option1"){
                                            include("../Backend/consultar_usuarios.php");
                                        }
                                        if($datos=="option2"){
                                            include("../Backend/consultar_usuarios_sensorActivado.php");
                                        }
                                        if($datos=="option3"){
                                            include("../Backend/consultar_usuarios_sensorInactivo.php");
                                        }
                                   
                                    ?>

                                    <!--   <tr>
                                        <td>Garrett Winters</td>
                                        <td>Accountant</td>
                                        <td>Tokyo</td>
                                        <td>63</td>
                                        <td>2011/07/25</td>
                                        <td>
                                            <div class="iconos-tabla-usuarios">

                                                <a href="#" data-toggle="modal" data-target="#eliminarUsuariotModal">
                                                   
                                                    <i class="fas fa-trash"></i>
                                                </a>

                                                <i class="fas fa-pen"></i>
                                            </div>
                                        </td>
                                    </tr>
                                    <tr>
                                        <td>Ashton Cox</td>
                                        <td>Junior Technical Author</td>
                                        <td>San Francisco</td>
                                        <td>66</td>
                                        <td>2009/01/12</td>
                                        <td>
                                            <div class="iconos-tabla-usuarios">

                                                <a href="#" data-toggle="modal" data-target="#eliminarUsuariotModal">
                                                   
                                                    <i class="fas fa-trash"></i>
                                                </a>

                                                <i class="fas fa-pen"></i>
                                            </div>
                                        </td>
                                    </tr>-->

                                </tbody>

                                <tfoot>
                                    <tr>
                                        <th>Usuario</th>
                                        <th>Nombre</th>
                                        <th>Apellido/s</th>
                                        <th>Sensor activo</th>
                                        <th>Ciudad</th>
                                        <th>Editar</th>
                                    </tr>
                                </tfoot>
                            </table>
                        </div>
                    </div>
                    <div class="card-footer small text-muted">Actualizada ayer a las 11:59 PM</div>
                </div>

            </div>
            <!-- /.container-fluid -->

            <!-- Footer -->
            <footer class="sticky-footer">
                <div class="container my-auto">
                    <div class="copyright text-center my-auto">
                        <span>Copyright © A Todo Gas 2020</span>
                    </div>
                </div>
            </footer>

        </div>
        <!-- /.content-wrapper -->

    </div>
    <!-- /#wrapper -->

    <!-- Scroll to Top Button-->
    <a class="scroll-to-top rounded" href="#page-top">
        <i class="fas fa-angle-up"></i>
    </a>

    <!-- Cerrar Sesión Modal-->
    <div class="modal fade" id="logoutModal" tabindex="-1" role="dialog" aria-labelledby="exampleModalLabel" aria-hidden="true">
        <div class="modal-dialog" role="document">
            <div class="modal-content">
                <div class="modal-header">
                    <h5 class="modal-title" id="exampleModalLabel">¿Listo para salir?</h5>
                    <button class="close" type="button" data-dismiss="modal" aria-label="Close">
                        <span aria-hidden="true">×</span>
                    </button>
                </div>
                <div class="modal-body">Selecciona 'Cerrar Sesión' si quieres salir realmente y ya ha sterminado todo tu trabajo.</div>
                <div class="modal-footer">
                    <button class="btn btn-secondary" type="button" data-dismiss="modal">Cancelar</button>
                    <a class="btn btn-primary" href="../Frontend/index.html">Cerrar Sesión</a>
                </div>
            </div>
        </div>
    </div>


    <!-- EliminarUsuario Modal-->
    <form class="modal fade" id="eliminarUsuariotModal" tabindex="-1" role="dialog" aria-labelledby="exampleModalLabel" aria-hidden="true" action="../Backend/borrar_Usuario.php" method="post">
        <div class="modal-dialog" role="document">
            <div class="modal-content">
                <div class="modal-header">
                    <h5 class="modal-title" id="exampleModalLabel">Eliminar Usuario</h5>
                    <button class="close" type="button" data-dismiss="modal" aria-label="Close">
                        <span aria-hidden="true">×</span>
                    </button>
                </div>
                <div class="modal-body">Desea eliminar este usuario de forma permanente</div>
                <div class="modal-footer" id="form-borrar-usuario">
                    <label for="city_id" class="control-label">Escribe el correo de la persona que quieres eliminar.</label>
                    <input type="text" class="form-control" id="city_id" name="idUsuario" placeholder="admin@admin.com" required>
                    <button class="btn btn-secondary" type="button" data-dismiss="modal">Cancel</button>
                    <button class="btn btn-primary" type="submit" id="boton-eliminar">Eliminar</button>
                </div>
            </div>
        </div>
    </form>

    <!-- Bootstrap core JavaScript-->
    <script src="vendor/jquery/jquery.min.js"></script>
    <script src="vendor/bootstrap/js/bootstrap.bundle.min.js"></script>

    <!-- Core plugin JavaScript-->
    <script src="vendor/jquery-easing/jquery.easing.min.js"></script>

    <!-- Page level plugin JavaScript-->
    <script src="vendor/chart.js/Chart.min.js"></script>
    <script src="vendor/datatables/jquery.dataTables.js"></script>
    <script src="vendor/datatables/dataTables.bootstrap4.js"></script>

    <!-- Custom scripts for all pages-->
    <script src="js/sb-admin.min.js"></script>

    <!-- Demo scripts for this page-->
    <script src="js/demo/datatables-demo.js"></script>
    <script src="js/demo/chart-area-demo.js"></script>

</body>

</html>
