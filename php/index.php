<?php
require_once("simple_html_dom.php");

function curl_download($smuID, $password, $Url) {
    // is cURL installed yet?
    if (!function_exists('curl_init')) {
        die('Sorry cURL is not installed!');
    }

    // OK cool - then let's create a new cURL resource handle
    $ch = curl_init();

    // Now set some options (most are optional)
    // Set URL to download
    curl_setopt($ch, CURLOPT_URL, $Url);

    // Set a referer
    curl_setopt($ch, CURLOPT_REFERER, "http://www.example.org/yay.htm");

    // User agent
    curl_setopt($ch, CURLOPT_USERAGENT, "MozillaXYZ/1.0");

    // Include header in result? (0 = yes, 1 = no)
    curl_setopt($ch, CURLOPT_HEADER, 0);

    // Should cURL return or print out the data? (true = return, false = print)
    curl_setopt($ch, CURLOPT_RETURNTRANSFER, true);

    // Timeout in seconds
    curl_setopt($ch, CURLOPT_TIMEOUT, 10);

    curl_setopt($ch, CURLOPT_FOLLOWLOCATION, true);

    //enable cookies
    $ckfile = tempnam("/tmp", "CURLCOOKIE");
    curl_setopt($ch, CURLOPT_COOKIEJAR, $ckfile);
    curl_setopt($ch, CURLOPT_RETURNTRANSFER, true);

    $qry_str = "userid=" . $smuID . "&pwd=" . $password;
    // Set request method to POST
    curl_setopt($ch, CURLOPT_POST, 1);

    // Set query data here with CURLOPT_POSTFIELDS
    curl_setopt($ch, CURLOPT_POSTFIELDS, $qry_str);

    // Download the given URL, and return output
    $output = curl_exec($ch);

    // Close the cURL resource, and free system resources
    curl_close($ch);

    $html = str_get_html($output);

    if (!$html) {
        echo"Didn't work";
    }

    foreach ($html->find('img') as $element) {
        if ($element->src == "/ps/images/PT_LOGIN_ERROR.gif") {
            http_response_code(204);
            //echo "bad";
        }
    }
    
    $classes_info = array();
    $ex = 0;
    foreach ($html->find('.PSLEVEL1GRIDNBO') as $classes) {
        

        foreach ($classes->find('tr') as $class) {
            
            if ($ex >= 2) {
                $class_info = array();
                $index = 0;
                foreach ($class->find('div') as $div) {
                    
                    ++$index;
                    if ($index == 2) {
                        array_push($class_info, str_replace("\r\n","",$div->plaintext));
                    }
                    if ($index == 7) {
                        foreach ($div->find('img') as $element) {
                            if ($element->src == "/cs/ps/cache/PT852/PS_CS_STATUS_OPEN_ICN_1.gif") {
                                array_push($class_info, "OPEN");
                            } else {
                                array_push($class_info, "NOT OPEN");
                            }
                        }
                    }
                    
                }
                array_push($classes_info,$class_info);
            }
            ++$ex;
            
        }
        
    }
    if($classes_info[0] != null){
        echo json_encode($classes_info);
    }
    
}

//echo curl_download("https://access.smu.edu/psp/ps/EMPLOYEE/HRMS/h/?tab=DEFAULT");
//echo( curl_download('https://access.smu.edu/psc/ps/EMPLOYEE/HRMS/c/SA_LEARNER_SERVICES.SSS_BROWSE_CATLG_P.GBL?Page=SSS_BROWSE_CATLG&Action=U'));

if (isset($_POST['id'])) {
    curl_download($_POST['id'], $_POST['pass'], 'https://access.smu.edu/psc/ps/EMPLOYEE/HRMS/c/SA_LEARNER_SERVICES.SSR_SSENRL_CART.GBL?Page=SSR_SSENRL_CART&Action=A&ACAD_CAREER=UG&EMPLID=37114094&ENRL_REQUEST_ID=&INSTITUTION=SMETH&STRM=1137');
}
    
//echo "<form action='index.php' method='POST'>
//    SMU ID: <input type='text' name='id'><br>
//    Password: <input type='password' name='pass'><br>
//    <input type='submit' value='Check My Classes!'><br>
//</form>";
    

?>
