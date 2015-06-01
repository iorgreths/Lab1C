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

/**
 * PBKDF2 key derivation function as defined by RSA's PKCS #5: https://www.ietf.org/rfc/rfc2898.txt
 * $algorithm - The hash algorithm to use. Recommended: SHA256
 * $password - The password.
 * $salt - A salt that is unique to the password.
 * $count - Iteration count. Higher is better, but slower. Recommended: At least 1000.
 * $key_length - The length of the derived key in bytes.
 * $raw_output - If true, the key is returned in raw binary format. Hex encoded otherwise.
 * Returns: A $key_length-byte key derived from the password and salt.
 *
 * Test vectors can be found here: https://www.ietf.org/rfc/rfc6070.txt
 *
 * This implementation of PBKDF2 was originally created by https://defuse.ca
 * With improvements by http://www.variations-of-shadow.com
 */
function pbkdf2($algorithm, $password, $salt, $count, $key_length, $raw_output = false) {
    $algorithm = strtolower($algorithm);
    if(!in_array($algorithm, hash_algos(), true))
        trigger_error('PBKDF2 ERROR: Invalid hash algorithm.', E_USER_ERROR);
    if($count <= 0 || $key_length <= 0)
        trigger_error('PBKDF2 ERROR: Invalid parameters.', E_USER_ERROR);

    if (function_exists("hash_pbkdf2")) {
        // The output length is in NIBBLES (4-bits) if $raw_output is false!
        if (!$raw_output) {
            $key_length = $key_length * 2;
        }
        return hash_pbkdf2($algorithm, $password, $salt, $count, $key_length, $raw_output);
    }

    $hash_length = strlen(hash($algorithm, "", true));
    $block_count = ceil($key_length / $hash_length);

    $output = "";
    for($i = 1; $i <= $block_count; $i++) {
        // $i encoded as 4 bytes, big endian.
        $last = $salt . pack("N", $i);
        // first iteration
        $last = $xorsum = hash_hmac($algorithm, $last, $password, true);
        // perform the other $count - 1 iterations
        for ($j = 1; $j < $count; $j++) {
            $xorsum ^= ($last = hash_hmac($algorithm, $last, $password, true));
        }
        $output .= $xorsum;
    }

    if($raw_output)
        return substr($output, 0, $key_length);
    else
        return bin2hex(substr($output, 0, $key_length));
}

function echoInsertSecure($username, $salt, $password) {
    $securePassword = pbkdf2(sha256, $password, $salt, 20000, 24);
    echo "SQL-Statement:<br/>";
    echo "<font style=\"font-family: monospace\"><b>INSERT INTO</b> users(username, salt, password)<br/><b>VALUES</b>(<br/>&nbsp;&nbsp;&nbsp;&nbsp;$username,<br/>&nbsp;&nbsp;&nbsp;&nbsp;$salt,<br/>&nbsp;&nbsp;&nbsp;&nbsp;$securePassword<br/></font>)";
}

if(isset($_POST['submit'])) {
    $username = htmlspecialchars($_POST["user"]);
    $password = htmlspecialchars($_POST["pass"]);
    $cpassword = htmlspecialchars($_POST["cpass"]);

    if ($password != $cpassword) {
        echo "<font color=\"red\"><b>Die beiden Passwörter müssen gleich sein!</b></font>";
    } else {
        $salt = mcrypt_create_iv(24, MCRYPT_DEV_URANDOM);
        echoInsertSecure($username, $salt, $password);
    }
}
?>
        </div>
    </body>
</html>