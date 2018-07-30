<?php
//var_dump($_SERVER);
$method = $_SERVER['REQUEST_METHOD'];//GET
$uri = $_SERVER['REQUEST_URI'];// /WebProjects/InfuraOfficeJavaFrontend/api/ddd/sss/ddd?eee=333
$body = file_get_contents("php://input");

$api = "http://localhost:8080/" . substr($uri, strlen("/WebProjects/InfuraOfficeJavaFrontend/"));

header("IOJ-DEBUG-METHOD: " . $method);
header("IOJ-DEBUG-URL: " . $api);
header("IOJ-DEBUG-BODY-LENGTH: " . strlen($body));

// create curl resource
$ch = curl_init();

// set url
curl_setopt($ch, CURLOPT_URL, $api);

//return the transfer as a string
curl_setopt($ch, CURLOPT_RETURNTRANSFER, 1);

curl_setopt($ch, CURLOPT_HEADER, 1);
curl_setopt($ch, CURLOPT_POST, 1);
curl_setopt($ch, CURLOPT_POSTFIELDS, $body);

// $output contains the output string
$output = curl_exec($ch);

// close curl resource to free up system resources
curl_close($ch);

header("IOJ-DEBUG-API-RESPONSE-LENGTH: " . strlen($output));

$lines = preg_split('/[\r\n]+/', $output);
//var_dump($lines);
$i = 0;
while ($i < count($lines)) {
    $line = $lines[$i];

    $content = json_decode($line);
    if ($content) {
        echo $line . PHP_EOL;
    } else {
        // header
        $parts = explode(": ", $line, 2);
        //var_dump($parts);
        if (count($parts) === 2) {
            header($line);
        }
    }

    $i++;
}

//echo output
//echo $output;

