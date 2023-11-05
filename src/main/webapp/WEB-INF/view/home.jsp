<!DOCTYPE html>
<%@ taglib uri = "http://java.sun.com/jsp/jstl/core" prefix = "c" %>
<%@ taglib prefix = "fmt" uri = "http://java.sun.com/jsp/jstl/fmt" %>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<html lang="en">
<head>
  <meta charset="utf-8">
  <meta name="viewport" content="width=device-width, initial-scale=1">
  <title>APEPDCL SOP Analasys</title>
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
<script>
function clearDiv(){
	 $('#cirAbsdiv').html('');
}
function getDivisonsAbs(selectionType,selectionName,selectionId){
	var peri=$('#peri').val();
	var perFinYear=$('#perFinYear').val();
	if(selectionType=='allcircles'){
	 $('#cirId').val('');
	 $('#cirName').val('');
	}else if(selectionType=='alldivisons'){
		 $('#cirId').val(selectionId);
		 $('#cirName').val(selectionName);
		 $('#divId').val('');
		 $('#divName').val('');
	}else if(selectionType=='allsubdivisons'){
		 $('#divId').val(selectionId);
		 $('#divName').val(selectionName);
		 $('#subDivId').val('');
		 $('#subDivName').val('');
	}else if(selectionType=='allsections'){
		 $('#subDivId').val(selectionId);
		 $('#subDivName').val(selectionName);
		 $('#secId').val('');
		 $('#secName').val('');
	}
	  var params=encodeURI('selectionType='+selectionType+'&selectionName='+selectionName+'&selectionId='+selectionId+'&peri='+peri+'&perFinYear='+perFinYear);
	  //alert(params);
	  $('#cirAbsdiv').html('<div align="center"><img src="dist/img/Spinner-3.gif" alt="progress..."/></div>');
	  $.ajax({
		 type : 'POST',
		 url : 'fuseOffReportAbsDiv',
		 data : params,
		 async : true,
		 cache : false,
		 success : function(response){
			 $('#cirAbsdiv').html(response);
		 }
	  });
}
function getBackDivisonsAbs(back_selectionType){
	var selectionId='';
	var selectionName='';
	if(back_selectionType=='allcircles'){
		 
		}else if(back_selectionType=='alldivisons'){
			selectionId=$('#cirId').val();
			selectionName=$('#cirName').val();
		}else if(back_selectionType=='allsubdivisons'){
			selectionId=$('#divId').val();
			selectionName=$('#divName').val();
		}else if(back_selectionType=='allsections'){
			selectionId=$('#subDivId').val();
			selectionName=$('#subDivName').val();
		}
	getDivisonsAbs(back_selectionType,selectionName,selectionId);
}
</script>
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

    <!-- Main content -->
    <section class="content">
      <div class="container-fluid">
      
      
        <div class="row">
          <div class="col-12">
           <div class="card card-success">
              <div class="card-header">
                <h3 class="card-title">Fuse-off Call</h3>
                <div class="card-tools">
                </div>
              </div>
			  <div class="card-tools col-12">
			  <div class="row">
                  <div class="input-group input-group-sm col-12" style="margin:5px;">
                  <div class="col-2" align="left">
                   <h4 class="card-title">Quarter : </h4>
                   </div>
                     <div class="col-3" align="left">
					<select id="peri" name="peri" class="col-12 form-control" onchange="clearDiv()">
						<option value="ALL">ALL</option>
						<option value="1">1st quarter</option>
						<option value="2">2nd quarter</option>
						<option value="3">3rd quarter</option>
						<option value="4">4th quarter</option>
					</select>
                  </div>
                    <div class="col-2" align="left">
                   <h4 class="card-title">Financial Year : </h4>
                   </div>
                     <div class="col-3" align="left">
                    <c:set var="tempFinYear" value="${fn:split(tempFinYear, '-')}"></c:set>
                    <select id="perFinYear" name="perFinYear" class="col-12 form-control" onchange="clearDiv()">
					<c:forEach begin="0" end="${tempFinYear[1]-2001}" varStatus="loop">
					<c:set var="minYY" value="${tempFinYear[0]-loop.index}"></c:set>
					<c:set var="maxYY" value="${fn:substring(tempFinYear[1]-loop.index,2,4)}"></c:set>
   					<option value="${minYY}-${maxYY}">${minYY}-${maxYY}</option>
					</c:forEach>
					</select>
                  </div>
                  <div class="col-2" align="left">
                  <button value="Submit" style="background-color: #066286;color: white;" onclick="getDivisonsAbs('allcircles')">Submit</button>
                   </div>
                </div>
                </div>
              </div>
                </div>
                </div>
           </div>
         <div class="row">
          <div class="col-12">
          	<input type="hidden" id="cirId" value=""/>
              <input type="hidden" id="divId" value=""/>
              <input type="hidden" id="subDivId" value=""/>
              <input type="hidden" id="secId" value=""/>
              <input type="hidden" id="cirName" value=""/>
              <input type="hidden" id="divName" value=""/>
              <input type="hidden" id="subDivName" value=""/>
              <input type="hidden" id="secName" value=""/>
           <div id="cirAbsdiv">
                
         <div class="row">
          <div class="col-12">
            <div class="card">
              <!-- /.card-header -->
              <div class="card-body table-responsive p-0">
                 <table class="table table-hover">
                  <thead>
                    <tr align="center">
                    <th rowspan="2">Sl.No.</th>
                    <th rowspan="2">${thName}</th>
                    <th colspan="5">URBAN</th>
                    <th colspan="5">RURAL</th>
                    </tr>
                   <tr align="center">
                    <th>Number of Complaints</th>
                    <th>Min Duration (Hrs)</th>
                    <th>Max Duration (Hrs)</th>
                    <th>Avg Duration (Hrs)</th>
                    <th>Complaints Resolved(%)</th>
                    <th>Number of Complaints</th>
                    <th>Min Duration (Hrs)</th>
                    <th>Max Duration (Hrs)</th>
                    <th>Avg Duration (Hrs)</th>
                    <th>Complaints Resolved(%)</th>
                  </tr>
                  </thead>
                  <tbody>
                  <c:choose>
                  <c:when test="${errCode eq '500'}">
                  <tr>
                    <td colspan="12"><span style="color: red;">Server Busy...</span></td>
                  </tr>
                  </c:when>
                  <c:when test="${errCode eq '100'}">
                  <tr>
                    <td colspan="12"><span style="color: red;">No Records Found...</span></td>
                  </tr>
                  </c:when>
                <c:otherwise>
                <%-- <c:set var="fld1Tot" value="0"/>
                <c:set var="fld2Tot" value="0"/>
                <c:set var="fld3Tot" value="0"/>
                <c:set var="fld4Tot" value="0"/>
                <c:set var="fld5Tot" value="0"/>
                <c:set var="fld6Tot" value="0"/>
                <c:set var="fld7Tot" value="0"/>
                <c:set var="fld8Tot" value="0"/>
                <c:set var="fld9Tot" value="0"/>
                <c:set var="fld10Tot" value="0"/> --%>
                
                  <c:forEach items="${list}" var="list" varStatus="indexing"> 
                    <tr>
                    <td>${indexing.index+1}</td>
                    <td><a style="cursor: pointer;color: blue;" onclick="getDivisonsAbs('${selectionType}','${list.selectionName}','${list.selectionId}')">${list.selectionName}</a></td>
                    <td align="center">${list.fld1}</td>
                    <td align="center">${list.fld2}</td>
                    <td align="center">${list.fld3}</td>
					<td align="center">${list.fld4}</td>
                    <td align="center">${list.fld5}</td>
                    <td align="center">${list.fld6}</td>
                    <td align="center">${list.fld7}</td>
                    <td align="center">${list.fld8}</td>
					<td align="center">${list.fld9}</td>
                    <td align="center">${list.fld10}</td>
               <%--  <c:set var="fld1Tot" value="${fld1Tot+list.fld1}"/>
                <c:set var="fld2Tot" value="${fld2Tot+list.fld2}"/>
                <c:set var="fld3Tot" value="${fld3Tot+list.fld3}"/>
                <c:set var="fld4Tot" value="${fld4Tot+list.fld4}"/>
                <c:set var="fld5Tot" value="${fld5Tot+list.fld5}"/>
                <c:set var="fld6Tot" value="${fld6Tot+list.fld6}"/>
                <c:set var="fld7Tot" value="${fld7Tot+list.fld7}"/>
                <c:set var="fld8Tot" value="${fld8Tot+list.fld8}"/>
                <c:set var="fld9Tot" value="${fld9Tot+list.fld9}"/>
                <c:set var="fld10Tot" value="${fld10Tot+list.fld10}"/> --%>
                  </tr>
                  </c:forEach>
                   <%-- <tr align="center">
                   <th align="center" colspan="2">APEPDCL Total</th>
                   <th align="center">${fld1Tot}</th>
                    <th align="center"><fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${fld2Tot}" /></th>
                    <th align="center"><fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${fld3Tot}" /></th>
					<th align="center"><fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${fld4Tot}" /></th>
                    <th align="center"><fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${fld5Tot}" /></th>
                    <th align="center">${fld6Tot}</th>
                    <th align="center"><fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${fld7Tot}" /></th>
                    <th align="center"><fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${fld8Tot}" /></th>
					<th align="center"><fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${fld9Tot}" /></th>
                    <th align="center"><fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${fld10Tot}" /></th>
                   </tr> --%>
                  </c:otherwise>
                    </c:choose>
                  </tbody>
                </table>
               
              </div>
              <!-- /.card-body -->
            </div>
            <!-- /.card -->
          </div>
          <!-- /.col -->
        </div>
        <!-- /.row -->
		<!-- row -->
		<div class="row">
        	<div class="col-12">
            	<div class="card card-success">
              <div class="card-header">
                <h3 class="card-title">Fuse-off Call</h3>
                <div class="card-tools">
                 	<label class="dropdown">
					  <div class="dd-button">
   					 <img src="dist/img/printer-icon-998.png" height="20" width="20">
 					 </div>
					  <input type="checkbox" class="dd-input" id="test">
					  <ul class="dd-menu">
    					<li id="download-img">Download image</li>
   						</ul>
  					</label>
                </div>
              </div>
              <div class="card-body">
                <div class="chart">
                  <canvas id="barChart" style="min-height: 250px; height: 250px; max-height: 250px; max-width: 100%;"></canvas>
                </div>
              </div>
              <!-- /.card-body -->
            </div>
			</div>
		</div>
		
		</div><!--cirAbsDiv end  -->
		</div>
		</div>
		
		
		
		<!-- /row -->
      </div>
      <!-- /.container-fluid -->
    </section>
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

<script>
var myChart='';
var responseList='${responseList}';
  $(function () {
	  var thNames=new Array();
	  var urTot=new Array();
	  var ruTot=new Array();
	  var urPer=new Array();
	  var ruPer=new Array();
	  var jsonArr = $.parseJSON(responseList);
	  $.each(jsonArr, function(i, obj) {
		  thNames.push(obj.selectionName);
		  urTot.push(obj.fld1);
		  ruTot.push(obj.fld6);
		  urPer.push(obj.fld5);
		  ruPer.push(obj.fld10);
		});
	  
	  
      var areaChartData = {
      labels  : thNames,
      datasets: [
        /* {
          label               : 'URBAN Complaint',
          backgroundColor     : 'rgba(60,141,188,0.9)',
          borderColor         : 'rgba(60,141,188,0.8)',
          pointRadius          : false,
          pointColor          : '#3b8bba',
          pointStrokeColor    : 'rgba(60,141,188,1)',
          pointHighlightFill  : '#fff',
          pointHighlightStroke: 'rgba(60,141,188,1)',
          data                : urTot
        },
        {
          label               : 'RURAL Complaints',
          backgroundColor     : 'rgba(210, 214, 222, 1)',
          borderColor         : 'rgba(210, 214, 222, 1)',
          pointRadius         : false,
          pointColor          : 'rgba(210, 214, 222, 1)',
          pointStrokeColor    : '#c1c7d1',
          pointHighlightFill  : '#fff',
          pointHighlightStroke: 'rgba(220,220,220,1)',
          data                : ruTot
        }, */
		
		{
          label               : 'Complaints Resolved(%) URBAN',
          backgroundColor     : 'rgba(210, 214, 102, 1)',
          borderColor         : 'rgba(210, 214, 222, 1)',
          pointRadius         : false,
          pointColor          : 'rgba(210, 214, 222, 1)',
          pointStrokeColor    : '#2FADB9',
          pointHighlightFill  : '#fff',
          pointHighlightStroke: 'rgba(220,220,220,1)',
          data                : urPer
        },
		{
          label               : 'Complaints Resolved(%) RURAL',
          backgroundColor     : 'rgba(210, 104, 222, 1)',
          borderColor         : 'rgba(210, 214, 222, 1)',
          pointRadius         : false,
          pointColor          : 'rgba(210, 214, 222, 1)',
          pointStrokeColor    : '#4397CB',
          pointHighlightFill  : '#fff',
          pointHighlightStroke: 'rgba(220,220,220,1)',
          data                : ruPer
        }
      ]
    };

    var areaChartOptions = {
      maintainAspectRatio : false,
      responsive : true,
      legend: {
        display: false
      },
      scales: {
        xAxes: [{
          gridLines : {
            display : false,
          }
        }],
        yAxes: [{
          gridLines : {
            display : false,
          }
        }]
      }
    };

  
    //-------------
    //- BAR CHART -
    //-------------
    var barChartCanvas = $('#barChart').get(0).getContext('2d');
    var barChartData = $.extend(true, {}, areaChartData);
    var temp0 = areaChartData.datasets[0];
    var temp1 = areaChartData.datasets[1];
    barChartData.datasets[0] = temp1;
    barChartData.datasets[1] = temp0;

    var barChartOptions = {
      responsive              : true,
      maintainAspectRatio     : false,
      datasetFill             : false
    };

     myChart=new Chart(barChartCanvas, {
      type: 'bar',
      data: barChartData,
      options: barChartOptions
    });
 
  });
  document.getElementById('download-img').onclick = function() {
  	  // Trigger the download
  	  var a = document.createElement('a');
  	  a.href = myChart.toBase64Image();
  	  a.download = 'image.png';
  	  a.click();
  	}
 
  
</script>

</body>
</html>
