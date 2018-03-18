package com.company;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.UpdateOptions;
import com.mongodb.client.result.UpdateResult;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Repository;

import static com.mongodb.client.model.Filters.eq;

@Repository
public class SyncRepository implements ApplicationListener<ContextRefreshedEvent> {

    @Autowired
    private MongoDatabase mongoDatabase;

    private MongoCollection<Document> coll;

    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        coll = mongoDatabase.getCollection("sync");
    }

    public String get(String uuid) {
        Document doc = coll.find(eq("_id", uuid)).first();

        return doc != null ?
                doc.toJson() :
                "";
    }

    public String update(String uuid, String data) {
        UpdateResult result = coll.replaceOne(
                eq("_id", uuid),
                Document.parse(data),
                new UpdateOptions().upsert(true));

        boolean existed = result.getMatchedCount() == 1;

        return existed ? "Updated" : "Inserted";
    }



}
