<?php

require_once("simple_html_dom.php");

function curl_download($smuID, $password, $Url) {
    $ch = curl_init();
    curl_setopt($ch, CURLOPT_URL, $Url);
    curl_setopt($ch, CURLOPT_REFERER, "http://www.example.org/yay.htm");
    curl_setopt($ch, CURLOPT_USERAGENT, "MozillaXYZ/1.0");
    curl_setopt($ch, CURLOPT_HEADER, 0);
    curl_setopt($ch, CURLOPT_RETURNTRANSFER, true);
    curl_setopt($ch, CURLOPT_TIMEOUT, 10);
    curl_setopt($ch, CURLOPT_FOLLOWLOCATION, true);
    $ckfile = tempnam("/tmp", "CURLCOOKIE");
    curl_setopt($ch, CURLOPT_COOKIEJAR, $ckfile);
    curl_setopt($ch, CURLOPT_RETURNTRANSFER, true);
    $qry_str = "userid=" . $smuID . "&pwd=" . $password;
    curl_setopt($ch, CURLOPT_POST, 1);
    curl_setopt($ch, CURLOPT_POSTFIELDS, $qry_str);
    $output = curl_exec($ch);
    curl_close($ch);

    $html = str_get_html($output);

    if (!$html) {
        echo"Didn't work";
    }

    echo $html;
}

//echo curl_download("https://access.smu.edu/psp/ps/EMPLOYEE/HRMS/h/?tab=DEFAULT");
//echo( curl_download('https://access.smu.edu/psc/ps/EMPLOYEE/HRMS/c/SA_LEARNER_SERVICES.SSS_BROWSE_CATLG_P.GBL?Page=SSS_BROWSE_CATLG&Action=U'));

if (isset($_GET['id'])) {
    echo $_GET['id'] . $_GET['pass'];
    curl_download($_GET['id'], $_GET['pass'], 'https://access.smu.edu/psc/ps/EMPLOYEE/HRMS/c/SA_LEARNER_SERVICES.SSR_SSENRL_CART.GBL?Page=SSR_SSENRL_CART&Action=A&ACAD_CAREER=UG&EMPLID=37114094&ENRL_REQUEST_ID=&INSTITUTION=SMETH&STRM=1137');
}
?>
