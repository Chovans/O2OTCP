#!/usr/bin
cd $1
#git config --global http.postBuffer 20971520
mkdir $2
cd $2
git clone $3 && cd $4 && git pull