result=`ssh -o StrictHostKeyChecking=no  -i ./test/src/main/resources/deploy/id_rsa  -p 11 root@timer.nessary.top "sh /opt/deploy/deploy_application.sh"`
echo $result
