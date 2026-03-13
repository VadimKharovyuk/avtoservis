package com.example.avtoservis.client;

import com.example.avtoservis.dto.ContactRequest.ContactRequestCreateDto;
import com.example.avtoservis.dto.ServiceItemResponseDto;
import com.example.avtoservis.enums.RequestType;

import com.example.avtoservis.admin.service.AdminContactRequestService;
import com.example.avtoservis.service.PublicServiceItemService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequiredArgsConstructor
public class PublicServiceController {

    private final PublicServiceItemService publicServiceItemService;
    private final AdminContactRequestService contactRequestService;

    // --- Деталі послуги за slug (SEO-friendly URL) ---
    @GetMapping("/sluzby/{slug}")
    public String serviceDetail(@PathVariable String slug, Model model) {
        ServiceItemResponseDto service = publicServiceItemService.getBySlug(slug);
        model.addAttribute("service", service);
        return "public/service-detail";
    }

    // --- Форма запису ---
    @GetMapping("/objednat")
    public String bookingForm(@RequestParam(required = false) Long service, Model model) {
        ContactRequestCreateDto dto = new ContactRequestCreateDto();
        dto.setRequestType(RequestType.SERVICE_ORDER);

        if (service != null) {
            dto.setServiceItemId(service);
        }

        model.addAttribute("contactRequest", dto);
        model.addAttribute("services", publicServiceItemService.getAllActive());
        return "public/booking";
    }

    // --- Відправка заявки ---
    @PostMapping("/objednat")
    public String submitBooking(@Valid @ModelAttribute("contactRequest") ContactRequestCreateDto dto,
                                BindingResult bindingResult,
                                Model model,
                                RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("services", publicServiceItemService.getAllActive());
            return "public/booking";
        }

        contactRequestService.create(dto);
        redirectAttributes.addFlashAttribute("success",
                "Vaše objednávka byla přijata! Budeme vás kontaktovat do 15 minut.");
        return "redirect:/";
    }
}
