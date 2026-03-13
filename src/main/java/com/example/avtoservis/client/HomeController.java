package com.example.avtoservis.client;

import com.example.avtoservis.admin.service.AdminServiceItemService;
import com.example.avtoservis.dto.ServiceItemResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@RequiredArgsConstructor
@Controller
public class HomeController {
     private final AdminServiceItemService serviceItemService ;

    @GetMapping
    public String home(Model model) {
        List<ServiceItemResponseDto> list = serviceItemService.getLatestServices();
        model.addAttribute("items", list);
        return "home";
    }
}
