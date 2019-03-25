#!/bin/bash

module=sms
deploy_dir=/opt/gea-server
start_log_dir=$deploy_dir/gea-log/SMS

cd $deploy_dir/$module
echo "Start $module"
/opt/gea-server/jdk1.8.0_181/bin/java -Dcom.sun.management.jmxremote -Dcom.sun.management.jmxremote.ssl=false -Dcom.sun.management.jmxremote.authenticate=false  -Xmx512m -XX:MaxPermSize=1024m -jar $deploy_dir/$module/$module.jar > $start_log_dir/console.log 2>&1 &

