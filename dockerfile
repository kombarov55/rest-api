FROM java:8
WORKDIR /app 
ADD target/carx-jar-with-dependencies.jar /app
EXPOSE 8080
CMD java -jar carx-jar-with-dependencies.jar
