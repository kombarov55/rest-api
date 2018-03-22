FROM java:8
WORKDIR /app 
COPY target/carx-jar-with-dependencies.jar /app
EXPOSE 80
CMD java -cp carx-jar-with-dependencies.jar com.company.App
