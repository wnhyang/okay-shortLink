FROM java:openjdk-8

RUN mkdir -p /okay-shortLink

WORKDIR /okay-shortLink

ARG JAR_FILE=./target/okay-shortLink-service.jar

COPY ${JAR_FILE} app.jar

EXPOSE 3301

ENV TZ=Asia/Shanghai JAVA_OPTS="-Xms128m -Xmx256m"

CMD sleep 60; java -jar app.jar $JAVA_OPTS
