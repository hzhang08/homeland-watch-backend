package com.homelandwatch.controller;

import com.homelandwatch.model.RequestDAO;
import com.homelandwatch.model.UserDAO;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
public class HelloController {

    @RequestMapping("/")
    public String index() {
        return "Greetings from Spring Boot!\n";
    }

    /*

    -askForRide(userId, location, destination, time)
    -askForShop(userId, location, destination, time)
    -askForCompanion(userId, location, destination, time)


    sendRequest(service, userId, location, destination, time)
    feedback(userId, volunteerUserId, feedback)

    List<Request> getOpenRequests(userId, location, startTime, endTime) // return all open requests within 5 miles
    Boolean acceptRequest(userId, request)
    Booelan requestFulfilled(userId,request)
    notifyFeedback(feedback)

    UserProfile describeUser(userId)
    Boolean updateProfile(userProfile)

     */

    @RequestMapping("/sendRequest")
    public UserDAO sendRequest(@RequestParam Optional<String> service, @RequestParam Optional<Integer> userId,
                                     @RequestParam Optional<Long> longtitude, @RequestParam Optional<Long> latitude,
                                     @RequestParam Optional<Long> endLongitude, @RequestParam Optional<Long> endLatitude,
                                     @RequestParam Optional<Long> startTime, @RequestParam Optional<Long> endTime) {

        return new UserDAO(42, "Wong", "http://example.com", 74,
                UserDAO.Gender.Female, 32, "San Jose", UserDAO.Role.Elderly);

    }

    @RequestMapping("/getOpenRequests")
    List<RequestDAO> getOpenRequests(@RequestParam Optional<Integer> userId, @RequestParam Optional<Long> longtitude,
                                     @RequestParam Optional<Long> latitude, @RequestParam Optional<Long> startTime,
                                     @RequestParam Optional<Long> endTime) {

        List<RequestDAO> requestArr = new ArrayList<>();

        requestArr.add(new RequestDAO(42, "Ride", 72,"David", 123,"Tim",
                3213, 4325, 4325,
                3243, 5432, 4563,
                RequestDAO.RequestStatus.Open));

        return requestArr;
    }// return all open requests within 5 miles






}