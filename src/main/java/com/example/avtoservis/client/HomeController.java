package com.example.avtoservis.client;

import com.example.avtoservis.admin.service.AdminServiceItemService;

import com.example.avtoservis.dto.ContactRequest.ContactRequestCreateDto;
import com.example.avtoservis.enums.Language;
import com.example.avtoservis.enums.RequestType;
import com.example.avtoservis.seo.HomePageSeoBuilder;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.LocaleResolver;


@RequiredArgsConstructor
@Controller
public class HomeController {
     private final AdminServiceItemService serviceItemService ;
     private final HomePageSeoBuilder homePageSeoBuilder;

    private final LocaleResolver localeResolver;


    @GetMapping("")
    public String root() {
        return "redirect:/cs";
    }

    @GetMapping("/{lang}")
    public String home(@PathVariable String lang,
                       Model model,
                       HttpServletRequest request,
                       HttpServletResponse response) {
        Language language = Language.fromCode(lang);

        // Встановлюємо locale для #{...}
        localeResolver.setLocale(request, response, language.toLocale());

        model.addAttribute("items", serviceItemService.getLatestServices(language));

        model.addAttribute("currentLang", language);
        model.addAttribute("languages", Language.getEnabledLanguages());
        model.addAttribute("seo", homePageSeoBuilder.buildHomePageSeo(language));

        model.addAttribute("services", serviceItemService.getAllActive(language));


        model.addAttribute("contactRequest", new ContactRequestCreateDto());

        ContactRequestCreateDto contactRequest = new ContactRequestCreateDto();
        contactRequest.setRequestType(RequestType.SERVICE_ORDER);

        return "home";
    }

    }

