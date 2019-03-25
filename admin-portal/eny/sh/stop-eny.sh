#!/bin/bash

module=eny
deploy_dir=/opt/gea-server

pid=$(ps -ef | grep "jar $deploy_dir/$module/$module.jar" | grep -v grep | awk '{print $2}')
echo "Stop $module"
kill -9 $pid
