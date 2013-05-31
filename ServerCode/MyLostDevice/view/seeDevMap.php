<!DOCTYPE html>
<html>
<title>Movil en el Mapa</title>
<head>
<meta name="viewport" content="width=device-width, initial-scale=1.0, user-scalable=no"/>
<meta charset="utf-8"/>

<style>
   html, body, #map_canvas {
     margin: 0;
     padding: 0;
     height: 100%;
   }
 </style>

<script type="text/javascript" src="https://maps.google.com/maps/api/js?sensor=true"></script>
<?php
	 $location = $_POST['location'];

    if($location!=null){
		
		  $lat = "";
		  $lng = "";

		  for($i=0; $i<strlen($location); $i++) { 
				
				if($location[$i] == ','){ $i += 2; break; }
				else $lat = $lat .  $location[$i];
		  }
		  for(; $i<strlen($location); $i++) { 
				$lng = $lng .  $location[$i];
		  }

        echo "
            <script type=\"text/javascript\">
            function initialize() {
					 var latlng = new google.maps.LatLng($lat,$lng);
					 var myOptions = {
						zoom: 15,
						center: latlng,
						mapTypeId: google.maps.MapTypeId.ROADMAP
					 };
					 var map = new google.maps.Map(document.getElementById('map_canvas'),
						  myOptions);
	
					var marker = new google.maps.Marker({    position: latlng,    title:'MOVIL'});

					marker.setMap(map);
	 		   }
            </script>
        ";
     }
?>

</head>
<body onload="initialize()">

<div id="map_canvas"></div>

</body>
</html>
