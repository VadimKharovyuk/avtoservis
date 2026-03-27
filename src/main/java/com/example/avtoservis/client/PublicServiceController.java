package com.example.avtoservis.client;

import com.example.avtoservis.dto.ContactRequest.ContactRequestCreateDto;
import com.example.avtoservis.dto.ServiceItemResponseDto;
import com.example.avtoservis.enums.Language;
import com.example.avtoservis.enums.RequestType;

import com.example.avtoservis.admin.service.AdminContactRequestService;
import com.example.avtoservis.service.PublicServiceItemService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequiredArgsConstructor
public class PublicServiceController {

    private final PublicServiceItemService publicServiceItemService;
    private final AdminContactRequestService contactRequestService;
    private final LocaleResolver localeResolver;

    @GetMapping("/{lang}/sluzby/{slug}")
    public String serviceDetail(@PathVariable String lang,
                                @PathVariable String slug,
                                Model model,
                                HttpServletRequest request,
                                HttpServletResponse response) {
        Language language = Language.fromCode(lang);
        localeResolver.setLocale(request, response, language.toLocale());

        ServiceItemResponseDto service = publicServiceItemService.getBySlug(slug, language);

        publicServiceItemService.viewServiceItem(service.getId());

        model.addAttribute("service", service);
        model.addAttribute("currentLang", language);
        model.addAttribute("languages", Language.getEnabledLanguages());
        return "public/service-detail";
    }


    @GetMapping("/{lang}/objednat")
    public String bookingForm(@PathVariable String lang,
                              @RequestParam(required = false) Long service,
                              Model model,
                              HttpServletRequest request,
                              HttpServletResponse response) {
        Language language = Language.fromCode(lang);
        localeResolver.setLocale(request, response, language.toLocale());

        ContactRequestCreateDto dto = new ContactRequestCreateDto();
        dto.setRequestType(RequestType.SERVICE_ORDER);

        if (service != null) {
            dto.setServiceItemId(service);
        }

        model.addAttribute("contactRequest", dto);
        model.addAttribute("services", publicServiceItemService.getAllActive(language)); // ← з мовою
        model.addAttribute("requestTypes", RequestType.values());
        model.addAttribute("currentLang", language);
        model.addAttribute("languages", Language.getEnabledLanguages());
        return "public/booking";
    }

    @GetMapping("/{lang}/sluzby")
    public String allServices(@PathVariable String lang,
                              Model model,
                              HttpServletRequest request,
                              HttpServletResponse response) {
        Language language = Language.fromCode(lang);
        localeResolver.setLocale(request, response, language.toLocale());

        model.addAttribute("services", publicServiceItemService.getAllActive(language));
        model.addAttribute("currentLang", language);
        model.addAttribute("languages", Language.getEnabledLanguages());
        return "public/services";
    }


    // ── Booking submit ──
    @PostMapping("/{lang}/objednat")
    public String submitBooking(@PathVariable String lang,
                                @Valid @ModelAttribute("contactRequest") ContactRequestCreateDto dto,
                                BindingResult bindingResult,
                                Model model,
                                RedirectAttributes redirectAttributes,
                                HttpServletRequest request,
                                HttpServletResponse response) {
        Language language = Language.fromCode(lang);
        localeResolver.setLocale(request, response, language.toLocale());

        if (bindingResult.hasErrors()) {
            model.addAttribute("services", publicServiceItemService.getAllActive());
            model.addAttribute("requestTypes", RequestType.values());
            model.addAttribute("currentLang", language);
            model.addAttribute("languages", Language.getEnabledLanguages());
            return "public/booking";
        }

        contactRequestService.create(dto);
        return "redirect:/" + lang + "/objednat/dekujeme";
    }

    // ── Thank You page ──
    @GetMapping("/{lang}/objednat/dekujeme")
    public String thankYou(@PathVariable String lang,
                           Model model,
                           HttpServletRequest request,
                           HttpServletResponse response) {
        Language language = Language.fromCode(lang);
        localeResolver.setLocale(request, response, language.toLocale());

        model.addAttribute("currentLang", language);
        model.addAttribute("languages", Language.getEnabledLanguages());
        return "public/thank-you";
    }
}
