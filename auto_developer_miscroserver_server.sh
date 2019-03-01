ss=`ssh -o StrictHostKeyChecking=no -i ./.deploy/id_rsa  -p 11 root@timer.nessary.top "ls -al /opt/"`
echo $ss

