package com.example.avtoservis.admin;

import com.example.avtoservis.enums.RequestStatus;
import com.example.avtoservis.repositoty.ContactRequestRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.avtoservis.repositoty.ServiceItemRepository;
import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {

    private final ServiceItemRepository serviceItemRepository;
     private final ContactRequestRepository contactRequestRepository;



    @GetMapping
    public String dashboard(Model model) {
        model.addAttribute("totalServices", serviceItemRepository.count());
        model.addAttribute("activeServices", serviceItemRepository.countByActiveTrue());
        model.addAttribute("totalViews", serviceItemRepository.sumViews());
         model.addAttribute("newRequests", contactRequestRepository.countByStatus(RequestStatus.NEW));


        return "admin/dashboard";
    }
}
