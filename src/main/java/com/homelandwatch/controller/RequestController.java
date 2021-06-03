package com.homelandwatch.controller;


import com.homelandwatch.model.RequestDAO;
import com.homelandwatch.model.UserDAO;
import com.homelandwatch.service.RequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
            model.addAttribute("requestDAO", new RequestDAO());
            return "send_request";
        }else{
            List<RequestDAO> listRequests = requestService.listAll();
            model.addAttribute("listRequests", listRequests);
            return "volunteer_home";
        }

    }


    @GetMapping("/newRequest")
    public String newRequest(Model model) {
        model.addAttribute("requestDAO", new RequestDAO());
        return "send_request";
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
        return "request_details";
    }

    @RequestMapping("/requests")
    public String viewHomePage(Model model) {
        List<RequestDAO> listRequests = requestService.listAll();
        model.addAttribute("listRequests", listRequests);
        return "view_request";
    }
}
