<!DOCTYPE html>
<%@ taglib uri = "http://java.sun.com/jsp/jstl/core" prefix = "c" %>
<%@ taglib prefix = "fmt" uri = "http://java.sun.com/jsp/jstl/fmt" %>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<html lang="en">
<head>
  <meta charset="utf-8">
  <meta name="viewport" content="width=device-width, initial-scale=1">
  <title>APEPDCL SOP Reports</title>
  <!-- Theme style -->
 <link rel="stylesheet" href="dist/css/adminlte.min.css">
  <!-- overlayScrollbars -->
  <link rel="stylesheet" href="dist/css/OverlayScrollbars.min.css">
   <style>
 .card-body>.table>thead>tr>td, .card-body>.table>thead>tr>th {
 border-top-width: 1px;
}
.dropdown {
  display: inline-block;
  position: relative;
  color:#252525;
  z-index:99;
  font-size:12px;
  font-weight:normal !important;
}

.dd-button {
  display: inline-block;
  /*border: 1px solid gray;
  border-radius: 4px;
  padding: 10px 30px 10px 20px;
  background-color: #ffffff;*/
  cursor: pointer;
  white-space: nowrap;
}

.dd-button:after {
  content: '';
  position: absolute;
  top: 50%;
  left: 15px;
  transform: translateY(-50%);
  width: 0; 
  height: 0; 
  border-left: 5px solid transparent;
  border-right: 5px solid transparent;
/*  border-top: 5px solid black;*/
}

.dd-input {
  display: none;
}

.dd-menu {
  position: absolute;
  top: 100%;
  border: 1px solid #ccc;
  border-radius: 4px;
  padding: 0;
  margin: 2px 0 0 0;
  box-shadow: 0 0 6px 0 rgba(0,0,0,0.1);
  background-color: #ffffff;
  list-style-type: none;
  margin-left:-150px;
}

.dd-input + .dd-menu {
  display: none;
} 

.dd-input:checked + .dd-menu {
  display: block;
} 

.dd-menu li {
  padding: 5px 20px;
  cursor: pointer;
  white-space: nowrap;
}

.dd-menu li:hover {
  background-color: rgb(51 92 173);
  color:#fff;
}

.dd-menu li a {
  display: block;
  margin: -10px -20px;
  padding: 10px 20px;
}

.dd-menu li.divider{
  padding: 0;
  border-bottom: 1px solid #cccccc;
}
</style>

</head>
<body class="hold-transition sidebar-mini layout-fixed">
<div class="wrapper">
    <jsp:include page="header.jsp" />
	<jsp:include page="sidemenu.jsp" />
  <!-- Content Wrapper. Contains page content -->
  <div class="content-wrapper">
    <!-- Content Header (Page header) -->
    <div class="content-header">
      <div class="container-fluid">
        <div class="row mb-2">        </div><!-- /.row -->
      </div><!-- /.container-fluid -->
    </div>
    <!-- /.content-header -->

    <!-- Main content --><div  style= "text-align: center;">
    <img src="dist/img/under_construction_PNG66.png" height="40%" width="25%" alt="" ></div>
    <!-- /.content -->
  </div>
  
  <!-- /.content-wrapper -->
<jsp:include page="footer.jsp" />
<jsp:include page="sidecontrol.jsp" />
  
</div>
<!-- ./wrapper -->

<!-- jQuery -->
<script src="dist/jquery/jquery.min.js"></script>

<!-- ChartJS -->
<script src="dist/chart.js/Chart.min.js"></script>

<!-- overlayScrollbars -->
<script src="dist/js/jquery.overlayScrollbars.min.js"></script>
<!-- AdminLTE App -->
<script src="dist/js/adminlte.js"></script>



</body>
</html>



