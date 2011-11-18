#!/bin/bash
if [ -e pid ] 
then
  pid=`cat pid`
  echo "Process already started with pid $pid"
else
  cd ~/workspaces/DreamSpell
  source config.local
  port=$port
  echo "Starting server on port: $port"  
  MONO_OPTIONS=--debug  xsp2 --nonstop --port $port &
  pid=$!
  echo $pid > pid
  echo "Started with pid: $pid"
  wait
fi
