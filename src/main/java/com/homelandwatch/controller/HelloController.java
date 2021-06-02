package com.homelandwatch.controller;

import com.homelandwatch.model.RequestDAO;
import com.homelandwatch.model.UserDAO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpSession;
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

    @RequestMapping("/login")
    public ResponseEntity login(@RequestParam Optional<String> username,
                                @RequestParam Optional<String> password,
                                HttpSession httpSession) {
//        if (sqliteAccessor.getUserByName(username).getPassword() == password) {
//            session.setAttribute("username", username); // username should be unique in this case
//            return new ResponseEntity(HttpStatus.ACCEPTED);
//        }

        return new ResponseEntity(HttpStatus.NOT_ACCEPTABLE);
    }

    @RequestMapping("/logout")
    public ResponseEntity logout(@RequestParam Optional<String> username,
                                 HttpSession httpSession) {
        if (!username.isPresent()) {
            return new ResponseEntity(HttpStatus.NOT_MODIFIED);
        }
        httpSession.removeAttribute(username.get());
        return new ResponseEntity(HttpStatus.ACCEPTED);
    }

    @RequestMapping("/sendRequest")
    public ResponseEntity<Integer> sendRequest(@RequestParam Optional<String> service, @RequestParam Optional<Integer> userId,
                                     @RequestParam Optional<Long> longtitude, @RequestParam Optional<Long> latitude,
                                     @RequestParam Optional<Long> endLongitude, @RequestParam Optional<Long> endLatitude,
                                     @RequestParam Optional<Long> startTime, @RequestParam Optional<Long> endTime) {

        // sqliteAccessor.insertRequest()
        // return requestId;
        return new ResponseEntity<Integer>(10, HttpStatus.ACCEPTED);

    }

    @RequestMapping("/getVolunteer")
    public ResponseEntity<Integer> getVolunteer(@RequestParam Optional<Integer> requestId) {
        // Request request = sqliteAccessor.getRequestById(requestId)
        // if (request.getRequestStatus == Accepted) return request.getVolunteerId();
        return new ResponseEntity<Integer>(20, HttpStatus.ACCEPTED);
        // else return new ResponseEntity<Integer>(20, HttpStatus.PROCESSING);
    }

    @RequestMapping("/acceptRequest")
    public ResponseEntity<Boolean> acceptRequest(@RequestParam Optional<Integer> userId,
            @RequestParam Optional<Integer> requestId) {
        // sqliteAccessor.updateRequestWithVolunteer(userId, requestId)
        return new ResponseEntity<Boolean>(true, HttpStatus.ACCEPTED);
    }

    @RequestMapping("/requestFulfilled")
    public ResponseEntity<Boolean> requestFulfilled(@RequestParam Optional<Integer> requestId) {
        // sqliteAccessor.updateRequestStatus(requestId, requestStatus)
        return new ResponseEntity<Boolean>(true, HttpStatus.ACCEPTED);
    }

    @RequestMapping("/getOpenRequests")
    List<RequestDAO> getOpenRequests(@RequestParam Optional<Integer> userId, @RequestParam Optional<Long> longtitude,
                                     @RequestParam Optional<Long> latitude, @RequestParam Optional<Long> startTime,
                                     @RequestParam Optional<Long> endTime) {

        // return sqliteAccessor.getOpenRequest(startTime)

        List<RequestDAO> requestArr = new ArrayList<>();

        requestArr.add(new RequestDAO(42, "Ride", 72, 123,
                3213, 4325, 4325,
                3243, 5432, 4563,
                RequestDAO.RequestStatus.Open));

        return requestArr;
    }// return all open requests within 5 miles






}