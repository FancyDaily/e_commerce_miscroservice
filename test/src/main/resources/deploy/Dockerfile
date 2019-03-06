FROM java
VOLUME /tmp
ADD ./build/libs/miscroservice*.jar /miscroservice.jar
ENV TZ=Asia/Shanghai
RUN ln -snf /usr/share/zoneinfo/$TZ /etc/localtime && echo $TZ > /etc/timezone
ENV  innerAddress=""
#ENTRYPOINT [ "sh", "-c", "java -Djava.security.egd=file:/dev/./urandom   -Deureka.instance.ip-address=$innerAddress -Djdbc.read.loadBalance=true -Dlog.path=/opt/logs/ -Xms126m -Xmx216m -jar /miscroservice.jar" ]
