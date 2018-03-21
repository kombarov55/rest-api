package com.company.repositories;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Repository;

import java.util.Date;

@Repository
public class ActivityRepository implements ApplicationListener<ContextRefreshedEvent> {


    private MongoCollection<Document> coll;


    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        coll = contextRefreshedEvent
                .getApplicationContext()
                .getBean(MongoDatabase.class)
                .getCollection("activity");
    }

    public void save(String uuid, int activity) {
        coll.insertOne(
                new Document("userId", uuid)
                .append("activity", activity)
                .append("date", new Date())
        );
    }

}
