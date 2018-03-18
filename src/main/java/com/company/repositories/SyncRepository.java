package com.company.repositories;

import com.company.controllers.ControllerException;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.UpdateOptions;
import com.mongodb.client.result.UpdateResult;
import org.bson.Document;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Repository;

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

        validateSize(data);

        Document doc = Document.parse(data);

        validateDocument(doc);

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

    /**
     * Размер json меньше 10Кб?
     * @param json json
     */
    private void validateSize(String json) {
        if (json.length() * 2 > (1024 * 10)) throw new ControllerException("Json size is greater than 10Kb");
    }

    private void validateDocument(Document document) {
        if (document.getInteger("money") == null) {
            throw new ControllerException("no \"money\" field in Json.");
        }

        if (document.get("country") == null) {
            throw new ControllerException("no \"country\" field in Json.");
        }
    }


    private static final String INSERTED = "Inserted";
    private static final String UPDATED = "Updated";

}
