<?php require_once("../controller_server/corUserSearch.php"); ?>

<!DOCTYPE html>

<html lang="es">
<head>
    <meta charset="utf-8" />

    <title> My Lost Device </title>

    <link rel="stylesheet" href="../css/style.css" />
	 <script type="text/javascript" src="../js/jquery.js"></script>

    <meta name="viewport" content="width=device-width , maximum-scale=1">

</head>

<body>

    <header>
    	<hgroup>
        	<h1>My Lost Device</h1>
        	<h2>Lista de Dispositivos - Se actualiza cada 5 seg</h2>
        </hgroup>
    </header>

    <section>
        <article id="formcontainer">

            <form name='input' action='logout.php' method='post'>
                <input type='submit' value='CERRAR SESION'>
            </form>

            <table id="tdevices">
                <tr>
                <th>Device</th>
                <th>Location</th>
                <th>State</th>
                <th>Opciones</th>
                </tr>
	           <?php
						error_reporting(E_ERROR);
						if($userid != -1){

							require_once("../controller_server/corDeviceSelect.php");

							$c = 1;


							while($row=mysql_fetch_array($deviceList)) {
								$devtype=$row["devType"];
								$devlocation=$row["devLocation"];
								$devstate=$row["devState"];

								echo "<tr>";

								if($devstate == 1)
									$devstate = 'Activado';
								else
									$devstate = 'Desactivado';

								echo "<td>$devtype</td><td>$devlocation</td><td>$devstate</td>";

								/*
								<form name='input' action='actDevAlarm.php' method='post'>
								<input name='userid' value='$userid' type='hidden'/>
								<input name='devid' value='$c' type='hidden'/>
								<input type='submit' value='Activar alarma'>
								</form>
								*/

								echo "<td>
								<form name='input' action='seeDevMap.php' method='post'>
								<input name='devid' value='$c' type='hidden'/>
								<input name='location' value='$devlocation' type='hidden'/>
								<input type='submit' value='Ver mapa'>
								</form>
								</td>";
								echo "</tr>";
								$c++;
							}
						}
					?>
    			</table>
		</article>
    </section>

<?php
echo "
<script type=\"text/javascript\">

	var pathname = window.location.pathname;
 	 setTimeout(function () {
        window.location = pathname + '?username=$username&password=$password';
    }, 5000);
</script>
"
?>

</body>
</html>
