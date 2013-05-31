<!DOCTYPE html>

<html lang="es">
<head>

    <meta charset="utf-8" />

    <title> My Lost Device </title>

    <link rel="stylesheet" href="../css/style.css" />

    <meta name="viewport" content="width=device-width , maximum-scale=1">

</head>

<body>

    <header>
    	<hgroup>
        	<h1>My Lost Device</h1>
        	<h2>Aplicacion para buscar un dispositivo Android</h2>
        </hgroup>
    </header>

    <section>

		<article id="formcontainer">
			<form name="formLogin" method="POST" action="../view/viewDevices.php" autocomplete="on"> 

                <div id="titulovista">
                	<h1>Log in</h1> 
                </div>

                <div id="logincomp"> 
                    <input name="username" required="required" type="text" placeholder="email"/>
                </div>
                <div id="logincomp">
                    <input name="password" if="password" required="required" type="password" placeholder="mypassword" /> 
                </div>
                <div id="logincomp"> 
                    <input type="submit" value="Login" /> 
	  			</div>
            </form>
		</article>
    </section>

</body>
</html>


