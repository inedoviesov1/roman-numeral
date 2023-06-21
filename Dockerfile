FROM eclipse-temurin:17-jdk-alpine

VOLUME /tmp/logs

ENV romanNumeralLogPath=/tmp/logs

COPY target/*.jar /opt/spring-boot/roman-numeral.jar
ENTRYPOINT ["java","-jar","/opt/spring-boot/roman-numeral.jar"]
