#!/bin/bash
cd ~/workspaces/DreamSpell
if [ -e pid ] 
then
  pid=`cat pid`
  kill -9 $pid
  rm pid
  echo "Stopped process $pid"
else
  echo "No process stop"
fi

