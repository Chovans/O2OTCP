#!/usr/bin
cd $1
#who
#cd ~/.ssh
#pwd
git config --global http.postBuffer 524288000
git clone $2 && cd $3 && git pull