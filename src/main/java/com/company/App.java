package com.company;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoDatabase;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan({"com.company"})
public class App {

    private static String DB_HOST = "mongo";
    private static int DB_PORT = 27017;


    public static void main(String[] args) {
        readHostAndPortFromArgs(args);
        new AnnotationConfigApplicationContext(App.class);
    }

    @Bean
    public MongoDatabase db() {
        System.out.println(String.format("creating MongoClient() at %s:%d", DB_HOST, DB_PORT));

        return new MongoClient(DB_HOST, DB_PORT).getDatabase("carx");
    }

    private static void readHostAndPortFromArgs(String[] args) {
        for (int i = 0; i < args.length; i += 2) {
            String key = args[i];
            String value = args[i + 1];

            if (key.equals("--dbhost")) DB_HOST = value;
            if (key.equals("--dbport")) DB_PORT = Integer.parseInt(value);
        }
    }
}
