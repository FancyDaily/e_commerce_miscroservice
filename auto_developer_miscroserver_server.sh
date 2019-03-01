local=`ls -al`
echo "$local"

script="ls ~/"

ss=`ssh -i ./.ssh/id_rsa  root@yjj.nessary.top "$script"`
echo $ss


