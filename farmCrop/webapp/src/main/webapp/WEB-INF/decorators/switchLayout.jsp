<!DOCTYPE html>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://www.opensymphony.com/sitemesh/decorator" prefix="decorator"%>
<%@ taglib uri="http://www.opensymphony.com/sitemesh/page" prefix="page"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<%@ taglib prefix="sb" uri="/struts-bootstrap-tags" %>

<%@ taglib uri="http://www.springframework.org/security/tags"
	prefix="sec"%>
<%@page import="com.sourcetrace.eses.umgmt.entity.Menu"%>
<%@page import="java.util.List"%>
<%@page import="java.util.Map"%>
<html lang="en" class="no-js">
<head>
<title>THE FARM CORP - The Next Generation Tech Solution</title>
<meta charset="utf-8" />
<meta name="viewport" content="width=device-width, initial-scale=1.0, user-scalable=0, minimum-scale=1.0, maximum-scale=1.0">
<meta name="apple-mobile-web-app-capable" content="yes">
<meta name="apple-mobile-web-app-status-bar-style" content="black">
<meta content="" name="description" />

<link href="assets/libs/admin-resources/jquery.vectormap/jquery-jvectormap-1.2.2.css" rel="stylesheet" type="text/css" />
<link rel="stylesheet" href="fonts/font-awesome/css/font-awesome.min.css">
<link rel="stylesheet" href="fonts/open-sans/css/open-sans.css">

<link rel="shortcut icon" href="/favicon.ico">
<link rel="apple-touch-icon" href="/apple-touch-icon.png">
<link rel="stylesheet" href="css/bootstrap.min.css">

<link rel="stylesheet" type="text/css" media="screen" href="assets/libs/jquery-ui-dist/jquery-ui.min.css" />
<link rel="stylesheet" href="css/theme-mobileView.css?v=3.2">
<link rel="shortcut icon" type="image/x-icon" href="login_populateLogo.action?logoType=favicon" />
<link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/jquery.perfect-scrollbar/0.6.7/css/perfect-scrollbar.min.css" />
<!-- jquery.vectormap css -->
<link href="assets/libs/admin-resources/jquery.vectormap/jquery-jvectormap-1.2.2.css" rel="stylesheet" type="text/css" />
<!-- DataTables -->
<link href="assets/libs/datatables.net-bs4/css/dataTables.bootstrap4.min.css" rel="stylesheet" type="text/css" />
<!-- Responsive datatable examples -->
<link href="assets/libs/datatables.net-responsive-bs4/css/responsive.bootstrap4.min.css" rel="stylesheet" type="text/css" />  
<!-- Bootstrap Css -->
<link href="assets/css/bootstrap.min.css" id="bootstrap-style" rel="stylesheet" type="text/css" />
<!-- Icons Css -->
<link href="assets/css/icons.min.css" rel="stylesheet" type="text/css" />
<!-- App Css-->
<link href="assets/css/app.min.css" id="app-style" rel="stylesheet" type="text/css" />
        
<script src="assets/libs/jquery/jquery.min.js"></script> 
<script src="assets/libs/twitter-bootstrap-wizard/bootstrap/js/bootstrap.min.js"></script>
<script src="js/bootstrap-hover-dropdown.min.js"></script>
<script src="js/jquery.mousewheel.js"></script>
<script src="js/remodal.min.js"></script>
<script src="js/perfect-scrollbar.js"></script>
<script type="text/javascript" src="https://cdnjs.cloudflare.com/ajax/libs/jquery.perfect-scrollbar/0.6.7/js/min/perfect-scrollbar.jquery.min.js"></script>
<script src="https://cdnjs.cloudflare.com/ajax/libs/select2/4.0.8/js/select2.min.js" defer></script>


<script src="js/theme-mobileView.js?v=5.2"></script>

  
  
</head>

<%-- <style>
@media screen and (min-width: 0px) and (max-width: 754px) {
      #div-mobile {    display: block;  }
      .div-desktop {    display: none;  }
      .wrapper {    display: block;  }
      .contentArea{ padding-left : 50px; }
      #div-mobile .dropdown-menu > li.user-header {
		    height: auto;
		    padding: 20px 10px;
		    text-align: center;
		    min-height: 150px;
		}

	.dashboardPageWrapper { margin-top:70px; }
	.headerBar { position: fixed;  z-index: 999999; width: 100%; padding-right:60px; }
}

@media screen and (min-width: 755px) and (max-width: 3000px) {
      #div-mobile {    display: none;  }
      .div-desktop {    display: block;  }

} 
</style> --%>

<body data-sidebar="dark">
	<%-- <script>
		try {
			if (typeof (Storage) !== "undefined") {
				if (localStorage.leftMenuPosition
						&& localStorage.leftMenuPosition == 0) {
					$('body').addClass('navigation-small');
				}
			}
		} catch (err) {
			console.log(err);
		}
	</script> --%>
	<%
		Map<String, Object> userInfo = (Map<String, Object>) session.getAttribute("USER_INFO");
		Long userId = (Long) userInfo.get("USER_REC_ID");
	%>
<%-- 	<script>

function isEmpty(val){
	  if(val==null||val==undefined||val.toString().trim()==""){
	   return true;
	  }
	  return false;
	}

function showPopup(content,msg)
{
	$(function () {
	   
	        $("#dialog").dialog({
	            modal: true,
	            title: msg,
	            width: 300,
	            height: 150,
	            hide: {
	                effect: "explode",
	                duration: 100
	              },
	            open: function (event, ui) {
	            	 var markup = content;
	                 $(this).html(markup);
	                setTimeout(function () {
	                    $("#dialog").dialog("close");
	                }, 2700);
	            }
	            
	           
	              
	        });
	        if(msg=='Error')
	        	 $(".ui-dialog").find(".ui-widget-header").css("background", "red");
          	else
          		 $(".ui-dialog").find(".ui-widget-header").css("background", "#41A1C9");
          	 
	        
	        
	  
	});
}	

$(function () {

$(".menuToggle > a").click(function(){
    $("body").toggleClass("showFixedSideMenu");
    if($("body").hasClass('showFixedSideMenu')) {
      var ct = 0;
      $(".submenu").each(function(){
        $(this).addClass('submenu'+ct);
        ct++;
      })
    }
    else {
      var submenuLength = $('.submenu').length;
      for(var i = 0; i<submenuLength; i++){
        $('.submenu').removeClass('submenu'+i);
      }
    }
  });
  $(".closeMnu").click(function(){
    $("body").toggleClass("showFixedSideMenu");
    var submenuLength = $('.submenu').length;
    for(var i = 0; i<submenuLength; i++){
      $('.submenu').removeClass('submenu'+i);
    }
  })
  
})
</script> --%>
<!-- Begin page -->
	 <div id="layout-wrapper">
	 
	 <header id="page-topbar">
                <div class="navbar-header">
                    <div class="d-flex">
                    <!-- LOGO -->
                             <div class="navbar-brand-box">
                             <a href="#" class="logo logo-light">
                                <span class="logo-sm">
                                    <img src="assets/images/small-logo.png" alt="" height="22">
                                </span>
                                <span class="logo-lg">
                                    <img src="assets/images/logo-light.png" alt="" height="20">
                                </span>
                            </a>
                        </div>
                        <button type="button" class="btn btn-sm px-3 font-size-24 header-item waves-effect" id="vertical-menu-btn">
                            <i class="ri-menu-2-line align-middle"></i>
                        </button>
                              
                        
                    </div>
                          <div class="d-flex">
                          
                              <!-- Language-->
                        <div class="dropdown d-none d-sm-inline-block">
                            <button type="button" class="btn header-item waves-effect"
                                data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
                                <img class="" src="assets/images/flags/us.jpg" alt="Header Language" height="16">
                            </button>
                            <div class="dropdown-menu dropdown-menu-right">
                    
                                <!-- item-->
                                <a href="javascript:void(0);" class="dropdown-item notify-item">
                                    <img src="assets/images/flags/spain.jpg" alt="user-image" class="mr-1" height="12"> <span class="align-middle">Spanish</span>
                                </a>

                                <!-- item-->
                                <a href="javascript:void(0);" class="dropdown-item notify-item">
                                    <img src="assets/images/flags/germany.jpg" alt="user-image" class="mr-1" height="12"> <span class="align-middle">German</span>
                                </a>

                                <!-- item-->
                                <a href="javascript:void(0);" class="dropdown-item notify-item">
                                    <img src="assets/images/flags/italy.jpg" alt="user-image" class="mr-1" height="12"> <span class="align-middle">Italian</span>
                                </a>

                                <!-- item-->
                                <a href="javascript:void(0);" class="dropdown-item notify-item">
                                    <img src="assets/images/flags/russia.jpg" alt="user-image" class="mr-1" height="12"> <span class="align-middle">Russian</span>
                                </a>
                            </div>
                        </div>
                           
                         
                        
                         <!-- fullscreen-->
                          <div class="dropdown d-none d-lg-inline-block ml-1">
                            <button type="button" class="btn header-item noti-icon waves-effect" data-toggle="fullscreen">
                                <i class="ri-fullscreen-line"></i>
                            </button>
                        </div>
                        
                         <!-- Profile-->
                <div class="dropdown d-inline-block user-dropdown">
                            <button type="button" class="btn header-item waves-effect" id="page-header-user-dropdown"
                                data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
                                <img class="rounded-circle header-profile-user" src="assets/images/users/avatar-2.jpg"
                                    alt="Header Avatar">
                                <span class="d-none d-xl-inline-block ml-1">Kevin</span>
                                <i class="mdi mdi-chevron-down d-none d-xl-inline-block"></i>
                            </button>
                            <div class="dropdown-menu dropdown-menu-right">
                                <!-- item-->
                                <a class="dropdown-item" href="#"><i class="ri-user-line align-middle mr-1"></i> Profile</a>
                                <a class="dropdown-item" href="#"><i class="ri-wallet-2-line align-middle mr-1"></i> My Wallet</a>
                                <a class="dropdown-item d-block" href="#"><span class="badge badge-success float-right mt-1">11</span><i class="ri-settings-2-line align-middle mr-1"></i> Settings</a>
                                <a class="dropdown-item" href="#"><i class="ri-lock-unlock-line align-middle mr-1"></i> Lock screen</a>
                                <div class="dropdown-divider"></div>
                                <a class="dropdown-item text-danger" href="#"><i class="ri-shut-down-line align-middle mr-1 text-danger"></i> Logout</a>
                            </div>
                        </div>
                          </div>
                        
                        
                  
                        
                     
        </div>          
                
    </header>
      <!-- ========== Left Sidebar Start ========== -->
      <div class="vertical-menu">

                <div data-simplebar class="h-100">

                    <!--- Sidemenu -->
                    <div id="sidebar-menu">
                        <!-- Left Menu Start -->
                       
                       
		<page:applyDecorator name="menu" page="/jsp/eseMenu.jsp" />
	
                      
                     </div>
                     
                  </div>
                  
     </div>
	 </div>
	  <!-- Left Sidebar End -->
	    <!-- ============================================================== -->
            <!-- Start right Content here -->
            <!-- ============================================================== -->
            <div class="main-content">
	  <div class="page-content">
                    <div class="container-fluid">

                        <!-- start page title -->
                        <div class="row">
                            <div class="col-12">
                                <div class="page-title-box d-flex align-items-center justify-content-between">
                                 <!--   <h4 class="mb-0"></h4> -->
                                    <div class="page-title-right">
                                        <ol class="breadcrumb m-0">
                                        <%
								List<Menu> menus = (List<Menu>) request.getAttribute("breadCrumb");
								if (menus != null) {
									for (Menu menu : menus) {
							%>
							
                                            <li class="breadcrumb-item"><a href="<%=menu.getUrl()%>"><%=menu.getLabel()%></a></li>
                                          <!--   <li class="breadcrumb-item active">Customers</li> -->
                                          <%
								}
								}
							%>
                                        </ol>
                                    </div>

                                </div>
                            </div>
                        </div>
                        <!-- end page title -->

                        <div class="row">
                            <div class="col-lg-12">
                                <div class="card">
                                    <div class="card-body">
                                    <decorator:body />
                                    </div>
                                 </div>
                            </div>
                         </div>
                      </div>
       </div>
     </div>            
 
	<%-- <div class="contentArea">
		<div class="flexbox-container">
			<div class="flexbox-item headerBar">
				<div class="header">
					<div class="inner-logo">
						<img src="login_populateLogo.action?logoType=app_logo" alt="logo" />
					</div>

					<div class="breadCrumb-wrapper div-desktop">
						<ul class="breadCrumbNavigation">
							<%
								List<Menu> menus = (List<Menu>) request.getAttribute("breadCrumb");
								if (menus != null) {
									for (Menu menu : menus) {
							%>
							<li><a href="<%=menu.getUrl()%>"><%=menu.getLabel()%></a></li>
							<%
								}
								}
							%>
							<span class="pull-right loggedBranch"><s:property
									value="#session.currentBranchName" /></span>
						</ul>
					</div>



					<div class="language-control div-desktop">
						<div class="btn-group">
							<button data-toggle="dropdown"
								class="btn btn-primary btn-hi-sts-transparent btn-sm">
								English <span class="caret"></span>
							</button>
							<ul class="dropdown-menu dropdown-menu-right dropdown-head">
								<%=(session.getAttribute("languageMenu"))%>
							</ul>
						</div>

						<div class="btn-group">
							<%=session.getAttribute("languageMenu")%>
						</div>

					</div>
					<div class="usr-image div-desktop">
						<img src="user_populateImage.action?id=<%=userId%>"
							class="circle-img avatar-rounded" alt="" height="30" width="30">

					</div>
					<div class="admin-area div-desktop">
						<ul class="nav">
							<li class="dropdown current-user">
								<button data-toggle="dropdown"
									class="btn btn-primary btn-hi-sts-transparent btn-sm"
									data-hover="dropdown" class="dropdown-toggle"
									data-close-others="true" href="#">
									<span class="username"><%=session.getAttribute("user")%>
										(<%=session.getAttribute("role")%>)</span> <i
										class="clip-chevron-down"></i>
								</button>

								<ul class="dropdown-menu dropdown-menu-right dropdown-head">
									<li><a href="user_detail.action?id=<%=userId%>"> <i
											class="fa fa-user"></i> &nbsp;<s:text name="myProfile" />
									</a></li>
									<li><a href="changePassword_edit.action"> <i
											class="fa fa-lock"></i> &nbsp;<s:text name="changePwd" />
									</a></li>
									<sec:authorize ifAllGranted="system.prefernces.list">
										<li><a href="prefernce_list.action"> <i
												class="fa fa-gear"></i> &nbsp;<s:text name="Settings" />
										</a></li>
									</sec:authorize>
									<li class="divider"></li>
									<li><a href="logout"> <i class="fa fa-sign-out"></i>
											&nbsp;<s:text name="logout" />
									</a></li>
								</ul>
							</li>



						</ul>
					</div>

					<div id="div-mobile">
						<div class="container">
									<div id="mySidepanel" class="sidepanel" >
  
  <a href="javascript:void(0)" class="closebtn" onclick="closeNav()"><i class="fa fa-times" aria-hidden="true"></i></a>
  
<div class="dropdown user user-menu open">
           
            <ul class="dropdown-menu">
              <!-- User image -->
              <li class="user-header">
                <img class="usr-image" src="user_populateImage.action?id=<%=userId%>" class="img-circle">
				
                <p>
                  <%=session.getAttribute("user")%> - (<%=session.getAttribute("userFullName")%>)
                  <small>Registered on date</small>
                </p>
                
                 <p>
                  <%=session.getAttribute("role")%> - (<%=session.getAttribute("branchId")%>)
                  <small>Registered on date</small>
                </p>
                
              </li>
              <!-- Menu Body -->
              <li class="user-body">
                 <div class="row">                  
                  <div class="col-xs-4 text-left">
                    <label>Language</label>
                   	
                   </div>
                  
                  
                   <div class="col-xs-8 text-left">
                    
                   	<div class="btn-group col-xs-12 langDropDown" style="padding: 0;width:137px !important;">
						<%=session.getAttribute("languageMenu")%>
					</div>
                   </div>
                  
                </div> 
                
                
                
                
                <div class="row settingsPanel">                  
                  <div class="col-xs-7 text-left">
                    <a href="changePassword_edit.action"><s:text name="changePwd" /></a>
                  </div>
                  
                  <div class="col-xs-5 text-left">
                    <a href="prefernce_list.action"><s:text name="Settings" /></a>
                    <a href="dashboard_list.action"><s:text name="Dashboard" /></a>
                  </div>
                  
                </div>
                
                <!-- /.row -->
              </li>
              <!-- Menu Footer-->
              <li class="user-footer clearfix">
                <div class="pull-left">
                  <a href="user_detail.action?id=<%=userId%>" class="btn btn-default btn-flat btn-info">Profile</a>
                </div>
                <div class="pull-right">
                  <a href="logout" class="btn btn-default btn-flat btn-warning"><s:text name="logout" /></a>
                </div>
              </li>
            </ul>
          </div>
</div>
						</div>
						<button class="openbtn" onclick="openNav()">
							<span class="glyphicon glyphicon-list"></span>
						</button>
					</div>

				</div>
			</div>
			
			-->
			
			
				<footer>
					<div class="footer clearfix">
						<div class="pull-right"> <div class="footer-inner">Powered by SourceTrace</div>	</div>				
					</div>
				</footer>
			</div>
		</div>
	
	
		<button type="button" id="enablePhotoModal"
		class="hide addBankInfo slide_open btn btn-sm btn-success"
		data-toggle="modal" data-target="#slideDPhotoModal" data-backdrop="static"
		data-keyboard="false">
		<i class="fa fa-plus" aria-hidden="true"></i>
		</button>

	<div id="slideDPhotoModal" class="modal fade" role="dialog">
		<div class="modal-dialog modal-sm">
			<!-- Modal content-->
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" id="model-close-DPhoto-btn" class="close hide"
						data-dismiss="modal">&times;</button>
					<h4 class="modal-title" id="mhead">
					</h4>
				</div>
				<div class="modal-body">
					<div id="myCarousel" class="carousel slide" data-ride="carousel">
						 <div class="carousel-inner" role="listbox" id="mbody">
						 	
						 </div>
						 
						 <a class="left carousel-control" href="#myCarousel" role="button" data-slide="prev">
						      <span class="glyphicon glyphicon-chevron-left" aria-hidden="true"></span>
						      <span class="sr-only">Previous</span>
   						 </a>
					    <a class="right carousel-control" href="#myCarousel" role="button" data-slide="next">
					      <span class="glyphicon glyphicon-chevron-right" aria-hidden="true"></span>
					      <span class="sr-only">Next</span>
					    </a>
					    
					</div>
				</div>
				<div class="modal-footer">
					<button type="button" class="btn btn-default"
						onclick="buttonDPhotoCancel()">
						<s:text name="close" />
					</button>
				</div>
			</div>

		</div>

	</div>
	
	
	
</div> --%>
	
	
	

	<script src="js/main.js"></script>

       
        <script src="assets/libs/bootstrap/js/bootstrap.bundle.min.js"></script>
        <script src="assets/libs/metismenu/metisMenu.min.js"></script>
        <script src="assets/libs/simplebar/simplebar.min.js"></script>
        <script src="assets/libs/node-waves/waves.min.js"></script>

        

        <!-- jquery.vectormap map -->
        <script src="assets/libs/admin-resources/jquery.vectormap/jquery-jvectormap-1.2.2.min.js"></script>
        <script src="assets/libs/admin-resources/jquery.vectormap/maps/jquery-jvectormap-us-merc-en.js"></script>

        <!-- Required datatable js -->
        <script src="assets/libs/datatables.net/js/jquery.dataTables.min.js"></script>
        <script src="assets/libs/datatables.net-bs4/js/dataTables.bootstrap4.min.js"></script>
        
        <!-- Responsive examples -->
        <script src="assets/libs/datatables.net-responsive/js/dataTables.responsive.min.js"></script>
        <script src="assets/libs/datatables.net-responsive-bs4/js/responsive.bootstrap4.min.js"></script>

       

     <%--    <script src="assets/js/app.js"></script> --%>
	 <script>
		jQuery(document).ready(function() {
			Main.init();
		});
	</script>
</body>
</html>