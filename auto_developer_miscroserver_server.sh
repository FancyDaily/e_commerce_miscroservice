#!/bin/bash

expect -c "

spawn ssh root@47.96.153.80 \"ls -al ~/;\"

expect {

\"*assword\" {set timeout 300; send \"Yujiejie123!@#\r\";}

\"yes/no\" {send \"yes\r\"; exp_continue;}

}

expect eof

"

`ssh -i ./.ssh/id_rsa  root@yjj.nessary.top "ls -al ~/"`


