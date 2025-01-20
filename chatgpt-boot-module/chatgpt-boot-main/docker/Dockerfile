# 使用官方的 OpenJDK 1.8 镜像作为基础镜像
FROM openjdk:8-jdk-alpine
VOLUME /logs
# 设置工作目录


# 将构建好的 JAR 文件复制到镜像中
ADD chatgpt.jar chatgpt.jar

# 设置环境变量以激活 prod 配置文件
ENV SPRING_PROFILES_ACTIVE=prod

# 暴露应用程序的端口
EXPOSE 9001

# 运行应用程序
#CMD java $JAVA_OPTS -jar chatgpt.jar
ENTRYPOINT ["/bin/sh", "-c", "java  -jar chatgpt.jar"]