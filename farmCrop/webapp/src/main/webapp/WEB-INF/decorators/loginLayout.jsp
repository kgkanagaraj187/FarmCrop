<!DOCTYPE html>
<%@page contentType="text/html; charset=UTF-8" %>  
<%@ taglib uri="http://www.opensymphony.com/sitemesh/decorator" prefix="decorator" %> 
<%@ taglib uri="http://www.opensymphony.com/sitemesh/page" prefix="page" %> 
<%@ taglib uri="/struts-tags" prefix="s" %>
<html lang="en" class="no-js">		
    <head>
        <title><decorator:title default="THE FARM CORP - The Next Generation Tech Solution"/></title> 
        <meta http-equiv="content-type" content="text/html; charset=utf-8"/>
        <meta http-equiv="Cache-Control" content="no-cache, no-store, must-revalidate"/>
        <meta http-equiv="Pragma" content="no-cache"/>
        <meta http-equiv="Expires" content="0"/>
        <meta http-equiv="X-Frame-Options" content="DENY" />
        <meta charset="utf-8" />		
        <meta name="viewport" content="width=device-width, initial-scale=1.0, user-scalable=0, minimum-scale=1.0, maximum-scale=1.0">

        <meta name="apple-mobile-web-app-capable" content="yes">
        <meta name="apple-mobile-web-app-status-bar-style" content="black">
        <meta content="The Next Generation Tech Solution" name="description" />
		<link rel="shortcut icon" href="/favicon.ico">
		<link rel="apple-touch-icon" href="/apple-touch-icon.png">
        <link rel="stylesheet" href="fonts/font-awesome/css/font-awesome.min.css">
        <link rel="stylesheet" href="fonts/open-sans/css/open-sans.css">

        <!-- <link rel="stylesheet" href="css/bootstrap.min.css"> -->
        <link rel="stylesheet" href="css/perfect-scrollbar.css">
        <link rel="stylesheet" href="css/remodal.css">
        <link rel="stylesheet" href="css/remodal-default-theme.css">
        <link rel="stylesheet" href="plugins/iCheck/skins/all.css">

        <link rel="stylesheet" href="css/responsive.css">
        <link rel="stylesheet" href="css/main.css">
        
        <link rel="stylesheet" href="css/bluewood.css">
       <link rel="stylesheet" href="css/theme.css">
        <link rel="shortcut icon" type="image/x-icon" href="login_populateLogo.action?logoType=favicon" />


        <script src="js/jquery.min.js"></script>
		<script src="js/remodal.min.js"></script>

 <!-- Bootstrap Css -->
        <link href="assets/css/bootstrap.min.css" id="bootstrap-style" rel="stylesheet" type="text/css" />
        <!-- Icons Css -->
        <link href="assets/css/icons.min.css" rel="stylesheet" type="text/css" />
        <!-- App Css-->
        <link href="assets/css/app.min.css" id="app-style" rel="stylesheet" type="text/css" />

    </head>

    <body class="auth-body-bg">
         <div class="container-fluid p-0">
                <div class="row no-gutters">
        <div class="col-lg-4">
           <!--  <div class="logo">
                <a class="center" href="#">
                    <img src="login_populateLogo.action?logoType=app_logo"/>
                </a>
            </div> -->

            <div>
                <decorator:body/>
            </div>

           
          </div>

<div class="col-lg-8">
                        <div class="authentication-bg">
                           <div id="carouselExampleIndicators" class="carousel slide" data-ride="carousel">
                                            <ol class="carousel-indicators">
                                                <li data-target="#carouselExampleIndicators" data-slide-to="0" class="active"></li>
                                                <li data-target="#carouselExampleIndicators" data-slide-to="1"></li>
                                                <li data-target="#carouselExampleIndicators" data-slide-to="2"></li>
                                            </ol>
                                            <div class="carousel-inner" role="listbox">
                                                <div class="carousel-item active">
                                                    <img class="d-block img-fluid" src="assets/images/authentication-bg.jpg" alt="First slide">
                                                </div>
                                                <div class="carousel-item">
                                                    <img class="d-block img-fluid" src="assets/images/authentication-bg.jpg" alt="Second slide">
                                                </div>
                                                <div class="carousel-item">
                                                    <img class="d-block img-fluid" src="assets/images/authentication-bg.jpg" alt="Third slide">
                                                </div>
                                            </div>
                                            <a class="carousel-control-prev" href="#carouselExampleIndicators" role="button" data-slide="prev">
                                                <span class="carousel-control-prev-icon" aria-hidden="true"></span>
                                                <span class="sr-only">Previous</span>
                                            </a>
                                            <a class="carousel-control-next" href="#carouselExampleIndicators" role="button" data-slide="next">
                                                <span class="carousel-control-next-icon" aria-hidden="true"></span>
                                                <span class="sr-only">Next</span>
                                            </a>
                                        </div>
                        </div>
                                       
                    </div>
 </div>
 </div>
 <!-- <div class="col-md-4 col-md-offset-9"></div> -->
    <!-- JAVASCRIPT -->
        <script src="assets/libs/jquery/jquery.min.js"></script>
        <script src="assets/libs/bootstrap/js/bootstrap.bundle.min.js"></script>
        <script src="assets/libs/metismenu/metisMenu.min.js"></script>
        <script src="assets/libs/simplebar/simplebar.min.js"></script>
        <script src="assets/libs/node-waves/waves.min.js"></script>
 		<script src="assets/js/pages/form-validation.init.js"></script>
        <script src="assets/js/app.js"></script>

        <script src="js/jquery.min.js"></script>
        <script src="js/bootstrap.min.js"></script>		
        <script src="js/bootstrap-hover-dropdown.min.js"></script>
        <script src="js/jquery.mousewheel.js"></script>		
        <script src="js/perfect-scrollbar.js"></script>
        <script src="plugins/iCheck/jquery.icheck.min.js"></script>
        <script src="js/jquery.validate.min.js"></script>
        <script src="js/main.js"></script>
        <script src="js/login.js"></script>
        <script>
            jQuery(document).ready(function () {
                Main.init();
                Login.init();
            });
        </script>
    </body>	
</html>