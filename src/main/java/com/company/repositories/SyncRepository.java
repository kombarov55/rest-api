package com.company.repositories;

import com.company.controllers.ControllerException;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.UpdateOptions;
import com.mongodb.client.result.UpdateResult;
import org.bson.Document;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Repository;

import java.util.Date;

import static com.mongodb.client.model.Filters.eq;

@Repository
public class SyncRepository implements ApplicationListener<ContextRefreshedEvent> {

    private MongoCollection<Document> coll;

    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        coll = contextRefreshedEvent
                .getApplicationContext()
                .getBean(MongoDatabase.class)
                .getCollection("sync");
    }

    public String saveOrUpdate(String uuid, String data) throws ControllerException {

        if (invalidSize(data)) throw new ControllerException("Json size is greater than 10Kb");

        Document doc = Document.parse(data);

        if (invalidDocStructure(doc)) throw new ControllerException("Json does not contain required 'money' or 'country fields'");

        if (coll.count(Filters.eq("_id", uuid)) == 0) {
            doc.append("creationDate", new Date());
        }

        UpdateResult result = coll.replaceOne(
                eq("_id", uuid),
                doc,
                new UpdateOptions().upsert(true));

        boolean existed = result.getMatchedCount() != 0;

        if (existed)
            return UPDATED;
        else
            return INSERTED;
    }

    public String get(String uuid) {
        Document doc = coll.find(eq("_id", uuid)).first();

        return doc != null ?
                doc.toJson() :
                "";
    }

    private boolean invalidSize(String json) {
        return json.length() * 2 > (1024 * 10);
    }

    private boolean invalidDocStructure(Document document) {
        return document.get("money") == null || document.get("country") == null;
    }


    private static final String INSERTED = "Inserted";
    private static final String UPDATED = "Updated";

}
