package com.example.avtoservis.client;

import com.example.avtoservis.admin.service.AdminServiceItemService;
import com.example.avtoservis.dto.ServiceItemResponseDto;
import com.example.avtoservis.enums.Language;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@RequiredArgsConstructor
@Controller
public class HomeController {
     private final AdminServiceItemService serviceItemService ;

    @GetMapping("")
    public String root() {
        return "redirect:/cs";
    }

    @GetMapping("/{lang}")
    public String home(@PathVariable String lang, Model model) {
        Language language = Language.fromCode(lang);
        List<ServiceItemResponseDto> list = serviceItemService.getLatestServices(language);
        model.addAttribute("items", list);
        model.addAttribute("currentLang", language);
        model.addAttribute("languages", Language.getEnabledLanguages());
        return "home";
    }
}
