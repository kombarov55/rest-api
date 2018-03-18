package com.company.controllers;

import com.company.repositories.ActivityRepository;
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

    public String createOrUpdateActivity(Request req, Response resp) {
        String uuid = req.queryParams("uuid");
        int activity = parseInt(req.queryParams("activity"));

        String result = activityRepository.createOrUpdate(uuid, activity);
        return buildResponse(result);
    }

}
