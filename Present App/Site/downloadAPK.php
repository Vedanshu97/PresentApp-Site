<?php
$file = "Present.apk";

if (file_exists($file)) {
    header('Content-Description: File Transfer');
    header('Content-Type: application/octet-stream');
    header('Content-Disposition: attachment; filename="'.basename($file).'"');
    header('Expires: 0');
    header('Cache-Control: must-revalidate');
    header('Pragma: public');
    header('Content-Length: ' . filesize($file));
    readfile($file);
    exit;
}
?>
<!-- START OF HIT COUNTER CODE --><br><script language="JavaScript" src="http://www.counter160.com/js.js?img=11"></script><br><a href="https://www.000webhost.com"><img src="http://www.counter160.com/images/11/left.png" alt="Free web hosting" border="0" align="texttop"></a><a href="http://www.hosting24.com"><img alt="Web hosting" src="http://www.counter160.com/images/11/right.png" border="0" align="texttop"></a><!-- END OF HIT COUNTER CODE --></body>