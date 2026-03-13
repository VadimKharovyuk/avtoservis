package com.example.avtoservis.admin.controller;

import com.example.avtoservis.admin.service.AdminContactRequestService;

import com.example.avtoservis.dto.ContactRequest.ContactRequestResponseDto;
import com.example.avtoservis.enums.RequestStatus;
import com.example.avtoservis.util.PageResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/admin/requests")
@RequiredArgsConstructor
public class AdminContactRequestController {

    private final AdminContactRequestService adminContactRequestService;

    // --- Список заявок (з фільтром по статусу) ---
    @GetMapping
    public String list(@RequestParam(required = false) RequestStatus status,
                       Pageable pageable,
                       Model model) {

        PageResponse<ContactRequestResponseDto> page = (status != null)
                ? adminContactRequestService.getByStatus(status, pageable)
                : adminContactRequestService.getAll(pageable);

        model.addAttribute("requests", page);
        model.addAttribute("currentStatus", status);
        model.addAttribute("statuses", RequestStatus.values());
        model.addAttribute("newCount", adminContactRequestService.countNew());
        return "admin/requests/list";
    }

    // --- Деталі заявки ---
    @GetMapping("/{id}")
    public String detail(@PathVariable Long id, Model model) {
        model.addAttribute("request", adminContactRequestService.getById(id));
        model.addAttribute("statuses", RequestStatus.values());
        return "admin/requests/detail";
    }

    // --- Зміна статусу ---
    @PostMapping("/{id}/status")
    public String updateStatus(@PathVariable Long id,
                               @RequestParam RequestStatus status,
                               RedirectAttributes redirectAttributes) {
        adminContactRequestService.updateStatus(id, status);
        redirectAttributes.addFlashAttribute("success", "Статус оновлено");
        return "redirect:/admin/requests/" + id;
    }

    // --- Видалення ---
    @PostMapping("/{id}/delete")
    public String delete(@PathVariable Long id,
                         RedirectAttributes redirectAttributes) {
        adminContactRequestService.deleteById(id);
        redirectAttributes.addFlashAttribute("success", "Заявку видалено");
        return "redirect:/admin/requests";
    }
}