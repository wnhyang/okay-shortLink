FROM openjdk:8-jre

# 挂载目录
VOLUME /home/wnh/apps
# 创建目录
RUN mkdir -p /home/wnh/apps/okay-shortLink
# 指定路径
WORKDIR /home/wnh/apps/okay-shortLink

ARG JAR_FILE=./jar/okay-shortLink-service.jar

COPY ${JAR_FILE} app.jar

EXPOSE 3301

ENV TZ=Asia/Shanghai JAVA_OPTS="-Xms128m -Xmx256m"

CMD sleep 60; java -jar app.jar $JAVA_OPTS
