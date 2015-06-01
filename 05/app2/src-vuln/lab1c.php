<!DOCTYPE html>
<html>
    <head>
        <meta charsert="UTF-8"/>
        <title>Registrieren</title>
    </head>
    <body id="body-color">
        <div id="Sign-Up">
            <fieldset style="width:30%">
                <legend>Registration Form</legend>
                <table border="0">
                    <form method="POST" action=<?php echo htmlspecialchars($_SERVER["PHP_SELF"]); ?>>
                            <td>Username: </td>
                            <td>
                                <input type="text" name="user"/>
                            </td>
                        </tr>
                        <tr>
                            <td>Passwort: </td>
                            <td>
                                <input type="password" name="pass"/>
                            </td>
                            </tr>
                        <tr>
                            <td>Bestätige das Passwort: </td>
                            <td>
                                <input type="password" name="cpass"/>
                            </td>
                        </tr>
                        <tr>
                            <td>
                                <input id="button" type="submit" name="submit" value="Registrieren"/>
                            </td>
                        </tr>
                    </form>
                </table>
            </fieldset>
        </div>
        <div>
            <br/>

<?php

function echoInsert($username, $password) {
    echo "SQL-Statement:<br/>";
    echo "<font style=\"font-family: monospace\"><b>INSERT INTO</b> users(username, password)<br/><b>VALUES</b>(<br/>&nbsp;&nbsp;&nbsp;&nbsp;$username,<br/>&nbsp;&nbsp;&nbsp;&nbsp;$password<br/></font>)";
}

if(isset($_POST['submit'])) {
    $username = htmlspecialchars($_POST["user"]);
    $password = htmlspecialchars($_POST["pass"]);
    $cpassword = htmlspecialchars($_POST["cpass"]);

    if ($password != $cpassword) {
        echo "<font color=\"red\"><b>Die beiden Passwörter müssen gleich sein!</b></font>";
    } else {
        echoInsert($username, $password);
    }
}
?>
        </div>
    </body>
</html>