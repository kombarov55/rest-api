package com.company.controllers;

import com.company.repositories.SyncRepository;
import com.mongodb.MongoWriteException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import spark.Request;
import spark.Response;

import static com.company.controllers.Utils.buildErrorResponse;
import static com.company.controllers.Utils.buildResponse;

@Controller
public class SyncController {

    @Autowired
    private SyncRepository syncRepository;

    public String getEntry(Request req, Response resp) {
        String uuid = req.queryParams("uuid");

        if (uuid == null) {
            return buildErrorResponse("No uuid parameter specified.");
        }

        String json = syncRepository.get(uuid);
        resp.status(json.isEmpty() ? 500 : 200);
        return json;
    }

    public String saveOrUpdateEntry(Request req, Response resp) {
        String uuid = req.queryParams("uuid");
        String data = req.queryParams("data");

        try {
            String status = syncRepository.saveOrUpdate(uuid, data);
            return buildResponse(status);
        } catch (ControllerException|MongoWriteException e) {
            resp.status(418);
            return buildErrorResponse(e.getMessage());
        }
    }



}
