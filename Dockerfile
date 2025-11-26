FROM openjdk:17-alpine
WORKDIR /app
COPY Calculator.java .
RUN javac Calculator.java
ENTRYPOINT ["java", "Calculator"]