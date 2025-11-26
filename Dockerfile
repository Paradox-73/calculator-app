FROM eclipse-temurin:17-jdk-alpine
WORKDIR /app
COPY Calculator.java .
RUN javac Calculator.java
ENTRYPOINT ["java", "Calculator"]