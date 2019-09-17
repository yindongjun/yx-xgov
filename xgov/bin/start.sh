#!/bin/bash
port=$1
if  [ ! -n "$port" ] ;then
    port=""
else
    port="--server.port=$port"
fi
nohup java -jar xgov-0.0.1-SNAPSHOT.jar $port > rolling.log 2>&1 &
echo $! > xgov.pid
echo "xgov started on port: "`cat xgov.pid`
