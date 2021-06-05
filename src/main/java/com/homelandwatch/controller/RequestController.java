package com.homelandwatch.controller;


import com.homelandwatch.model.RequestDAO;
import com.homelandwatch.model.UserDAO;
import com.homelandwatch.service.RequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.homelandwatch.app.Constants.DAVID_ID;
import static com.homelandwatch.app.Constants.TIM_ID;

@Controller
public class RequestController {

    @Autowired
    private RequestService requestService;

    @GetMapping("/")
    public String index(Model model) {
        return "index";
    }


    @PostMapping("/login")
    public String login(@RequestParam("username") String username,
                        Model model) {
        if("David".equalsIgnoreCase(username)){
            List<RequestDAO> listRequests = requestService.listAllElderlyRequests(DAVID_ID);
            model.addAttribute("listRequests", listRequests);
            return "elderly_home";
        }else{
            List<RequestDAO> openRequests = requestService.listAll();
            List<RequestDAO> myAcceptedRequest = requestService.listMyAcceptedRequets(TIM_ID);
            model.addAttribute("openRequests", openRequests);
            model.addAttribute("myAcceptedRequest", myAcceptedRequest);
            return "volunteer_home";
        }

    }


    @GetMapping("/newRequest")
    public String newRequest(Model model) {
        model.addAttribute("requestDAO", new RequestDAO());
        return "request_details";
    }

    @PostMapping("/sendRequest")
    public String sendRequest(@ModelAttribute RequestDAO requestDAO,
                              Model model) {
        model.addAttribute("requestDAO",requestDAO);
        //save to db
        requestDAO.setElderlyId(42);
        requestDAO.setElderlyName("David");
        requestDAO.setRequestStatus(RequestDAO.RequestStatus.Open);
        requestService.save(requestDAO);
        List<RequestDAO> listRequests = requestService.listAllElderlyRequests(DAVID_ID);
        model.addAttribute("listRequests", listRequests);
        return "elderly_home";
    }

    @RequestMapping("/acceptRequest")
    public String acceptRequest(@RequestParam("requestId") int requestId,Model model) {
        requestService.accept(requestId,TIM_ID);
        List<RequestDAO> openRequests = requestService.listAll();
        List<RequestDAO> myAcceptedRequest = requestService.listMyAcceptedRequets(TIM_ID);
        model.addAttribute("openRequests", openRequests);
        model.addAttribute("myAcceptedRequest", myAcceptedRequest);
        return "volunteer_home";
    }
}
