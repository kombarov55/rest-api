package com.company;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoDatabase;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Configuration
@ComponentScan({"com.company"})
public class App {

    private static String HOST = "mongo-server";
    private static int PORT = 27017;


    public static void main(String[] args) {
        new AnnotationConfigApplicationContext(App.class);
    }

    @Bean
    public MongoDatabase db() {
        return new MongoClient(HOST, PORT).getDatabase("carx");
    }
}
