<style>
  
  /* New Logo CSS */
.logo {
	position: relative;
	/*  margin-top: -5px; */
}

.logo img {
	width: 100% !important;
}

.logo a {
	/*  margin: 5px 0 0px 0; */
	display: block;
}

.logo::after {
	background: url("dist/img/apepdcl_old1.png");
	height: 96px;
	width: 100px;
	position: absolute;

	top: -15px;
	content: "";
}

.logo h4 {
	font-size: 1.2em;
	color: #733804;
	text-align: center;
	font-weight: bold;
	text-transform: capitalize;
	margin-top: -10px;
	margin-bottom: 0px;
	line-height: 16px;
}

.logo h4 span {
	font-size: 10px;
	display: inline;
	color: #333;
	text-align: center;
	font-weight: 500;
	line-height: 16px;
}

.header-top-block {
	background: #e7e7e7;
	text-align: right;
	width: 100%;
	border-bottom: 1px solid #ccc;
}

.top-bar-block ul {
	margin: 0;
	padding-top: 10px;
	float: left;
}

.top-bar-block ul li {
	line-height: 24px;
}

.top-bar-block ul li p {
	font-size: 20px;
	color: #343f79;
	font-weight: 400;
	margin: 0px;
	font-family: "Roboto", sans-serif;
}

.top-bar-block button {
	padding: 2px 12px;
	border: 0px;
	font-family: sans-serif;
	color: #fff;
	text-decoration: none;
/* 	text-transform: capitalize; */
	font-size: 16px;
	display: block;
	overflow: hidden;
	border-radius: 6px;
	background: #066e96;
	margin: 2px 0;
	font-weight: 500;
}

.top-bar-block button:hover {
	color: #000;
	background: #cedce0;
	transition: all 0.24s ease-in-out;
	text-decoration: none;
}

.top-bar-block ul li p {
	font-size: 16px;
}

.top-bar-block ul li p span a {
	font-size: 20px;
	color: #d61d09;
}

.gstin {
	font-size: 12px !important;
	color: #066e96 !important;
	line-height: 16px;
	font-weight: 600 !important;
}

.gstin span {
	font-size: 14px !important;
}

th{
    background-color: #066286;
    color: white;
}
  
  </style>
<!-- Preloader -->
  <div class="preloader flex-column justify-content-center align-items-center">
    <img class="animation__shake" src="dist/img/AdminLTELogo.png" alt="AdminLTELogo" height="60" width="60">  </div>

  <!-- Navbar -->
  <nav class="main-header navbar navbar-expand navbar-white navbar-light">
    <!-- Left navbar links -->
    <ul class="navbar-nav">
      <li class="nav-item">
        <a class="nav-link" data-widget="pushmenu" href="#" role="button"><img src="dist/img/4453896.png" width="30" height="30"></a>      </li>
      <li class="nav-item d-none d-sm-inline-block">
       <div class="logo col-md-8 col-sm-8 col-xs-12">
						<a href="home"><img src="dist/img/logo_new12.png" height="20%" width="80%" alt="" class="img-responsive"></a>
						<h4>
							Eastern Power Distribution Company of AP Limited </br><span>(A
								Govt. of A.P.Enterprise & An ISO 9001:2015 & ISO 27001:2013
								Certified Company) <span class="cin-cont">CIN:U40109AP2000SGC034117</span></span>						</h4>
					</div>
      </li>
      <!--<li class="nav-item d-none d-sm-inline-block">
        <a href="#" class="nav-link">Contact</a>
      </li> -->
    </ul>
    <!-- Right navbar links -->
  </nav>
  <!-- /.navbar -->