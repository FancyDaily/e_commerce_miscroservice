`chmod -R 650  ./id_rsa`


script="ls ~/"

ss=`ssh -i ./id_rsa  root@yjj.nessary.top "$script"`
echo $ss


