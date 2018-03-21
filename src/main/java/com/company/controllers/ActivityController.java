package com.company.controllers;

import com.company.repositories.ActivityRepository;
import com.mongodb.MongoWriteException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import spark.Request;
import spark.Response;

import static com.company.controllers.Utils.buildResponse;
import static java.lang.Integer.parseInt;

@Controller
public class ActivityController {

    @Autowired
    private ActivityRepository activityRepository;

    public String saveActivity(Request req, Response resp) {
        String uuid = req.queryParams("uuid");
        int activity = parseInt(req.queryParams("activity"));

        try {
            activityRepository.save(uuid, activity);
            return buildResponse("Inserted");
        } catch (MongoWriteException e) {
            resp.status(418);
            return buildResponse("Failed to write to database");
        }
    }

}
