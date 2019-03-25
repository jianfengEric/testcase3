#!/bin/bash

module=eny
deploy_dir=/opt/gea-server
start_log_dir=$deploy_dir/gea-log/ENY

cd $deploy_dir/$module
echo "Start $module"
/opt/gea-server/jdk1.8.0_181/bin/java -jar $deploy_dir/$module/$module.jar --spring.profiles.active=sit -Xmx512m -XX:MaxPermSize=1024m > $start_log_dir/console.log 2>&1 &

