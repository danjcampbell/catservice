FROM openjdk:11

ADD target/cat-service.jar cat-service.jar
EXPOSE 8085
ENTRYPOINT ["java","-jar","cat-service.jar"]