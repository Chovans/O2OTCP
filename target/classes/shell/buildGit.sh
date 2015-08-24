#!/usr/bin
cd $1
git config --global http.postBuffer 20971520
git clone $2 && cd $3 && git pull