<%@ taglib uri = "http://java.sun.com/jsp/jstl/core" prefix = "c" %>
<%@ taglib prefix = "fmt" uri = "http://java.sun.com/jsp/jstl/fmt" %>
<div>
<c:if test="${backBtn eq 'yes'}">
<div align="left"><button value="Back" style="background-color: #066286;color: white;" onclick="getBackDivisonsAbs('${back_selectionType}')">Back</button> </div>
 </c:if>        
             <div class="row">
          <div class="col-12">
            <div class="card">
              <!-- /.card-header -->
              <div class="card-body table-responsive p-0">
                <table class="table table-hover">
                  <thead>
                    <tr align="center">
                    <th rowspan="2" style="vertical-align: middle;">Sl.No.</th>
                    <th rowspan="2" style="vertical-align: middle;" >${thName}</th>
                   	<th>No. of Complaints</th>
                    <th>Minimum Duration (Days)</th>
                    <th>Maximum Duration (Days)</th>
                    <th>Average Duration (Days)</th>
                    <th>Complaints Resolved within Service Level (%)</th>
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
               <%--  <c:set var="fld1Tot" value="0"/>
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
                    <td align="right">${indexing.index+1}</td>
                    <td align="left">
                    <c:choose>
                    <c:when test="${selectionType eq 'allDetails'}">
                    ${list.fld6}
                    </c:when>
                    <c:otherwise>
                    <a style="cursor: pointer;color: blue;" onclick="getDivisonsAbs('${selectionType}','${list.fld6}','${list.fld7}')">${list.fld6}</a>
                    </c:otherwise>
                    </c:choose>
                    
                    <td align="right">${list.fld1}</td>
                    <td align="right">${list.fld2}</td>
                    <td align="right">${list.fld3}</td>
<%--                     <td align="right"><fmt:formatNumber maxFractionDigits="2" minFractionDigits="2" type = "number" value = "${list.fld2}"/></td> --%>
<%--                     <td align="right"><fmt:formatNumber maxFractionDigits="2" minFractionDigits="2" type = "number" value = "${list.fld3}"/></td> --%>
					<td align="right"><fmt:formatNumber maxFractionDigits="2" minFractionDigits="2" type = "number" value = "${list.fld4}"/></td>
                    <td align="right"><fmt:formatNumber maxFractionDigits="2" minFractionDigits="2" type = "number" value = "${list.fld5}"/></td>
                    
              <%--   <c:set var="fld1Tot" value="${fld1Tot+list.fld1}"/>
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
                <h3 class="card-title">Replacement of Burnt meters - Consumer</h3>
                <div class="card-tools">
                  <label class="dropdown">
					  <div class="dd-button">
   					 <img src="dist/img/printer-icon-998.png" height="20" width="20">
 					 </div>
					  <input type="checkbox" class="dd-input" id="test">
					  <ul class="dd-menu">
    					<li id="download-img1">Download image</li>
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
   </div>

<script>
var myChart1='';
var responseList='${responseList}';
  $(function () {
	  var thNames=new Array();
	  var urTot=new Array();
	  var ruTot=new Array();
	  var urPer=new Array();
	  var ruPer=new Array();
	  var jsonArr = $.parseJSON(responseList);
	  $.each(jsonArr, function(i, obj) {
		  thNames.push(obj.fld6);
		  urTot.push(obj.fld5);
		 
		});
	  
	  
      var areaChartData = {
      labels  : thNames,
      datasets: [
       /*  {
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
		
        /* {
            label               : 'Complaints Resolved(%) URBAN',
            backgroundColor     : 'rgba(230, 126, 0, 1)',
            borderColor         : 'rgba(230, 126, 0, 1)',
            pointRadius         : false,
            pointColor          : 'rgba(230, 126, 0, 1)',
            pointStrokeColor    : '#2FADB9',
            pointHighlightFill  : '#fff',
            pointHighlightStroke: 'rgba(220,220,220,1)',
            data                : urPer
          }, */
  		{
            label               : 'Complaints Resolved(%) RURAL',
            backgroundColor     : 'rgba(0, 107, 179, 1)',
            borderColor         : 'rgba(0, 107, 179, 1)',
            pointRadius         : false,
            pointColor          : 'rgba(0, 107, 179, 1)',
            pointStrokeColor    : '#4397CB',
            pointHighlightFill  : '#fff',
            pointHighlightStroke: 'rgba(220,220,220,1)',
            data                : urTot
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
    barChartData.datasets[0] = temp0;

    var barChartOptions = {
      responsive              : true,
      maintainAspectRatio     : false,
      datasetFill             : false,
      scales: {
          yAxes: [{
            ticks: {
              beginAtZero: true
            }
          }]
        }
    };

    myChart1=new Chart(barChartCanvas, {
      type: 'bar',
      data: barChartData,
      options: barChartOptions
    });

  });
  document.getElementById('download-img1').onclick = function() {
  	  // Trigger the download
  	  var a = document.createElement('a');
  	  a.href = myChart1.toBase64Image();
  	  a.download = 'image.png';
  	  a.click();
  	}
  
  
</script>
   