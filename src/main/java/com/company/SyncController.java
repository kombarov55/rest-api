package com.company;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import spark.Request;
import spark.Response;

@Controller
public class SyncController {

    @Autowired
    private SyncRepository syncRepository;

    public String getSyncEntry(Request req, Response resp) {
        String uuid = req.queryParams("uuid");

        String json = syncRepository.get(uuid);
        resp.status(json.isEmpty() ? 200 : 500);
        return json;
    }

    public String updateSyncEntry(Request req, Response resp) {
        String uuid = req.queryParams("uuid");
        String data = req.queryParams("data");
        return syncRepository.update(uuid, data);
    }
}
