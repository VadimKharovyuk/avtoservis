package com.example.avtoservis.admin.controller;
import com.example.avtoservis.admin.service.AdminServiceItemService;
import com.example.avtoservis.dto.ServiceItemCreateDto;
import com.example.avtoservis.dto.ServiceItemResponseDto;
import com.example.avtoservis.dto.ServiceItemUpdateDto;
import com.example.avtoservis.util.PageResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/admin/services")
@RequiredArgsConstructor
public class AdminServiceItemController {

    private final AdminServiceItemService adminServiceItemService;

    // --- Список услуг ---
    @GetMapping
    public String list(Pageable pageable, Model model) {
        PageResponse<ServiceItemResponseDto> page = adminServiceItemService.getAll(pageable);
        model.addAttribute("services", page);
        return "admin/services/list";
    }

    // --- Форма создания ---
    @GetMapping("/create")
    public String createForm(Model model) {
        model.addAttribute("serviceItem", new ServiceItemCreateDto());
        return "admin/services/create";
    }

    // --- Сохранение новой услуги ---
    @PostMapping("/create")
    public String create(@Valid @ModelAttribute("serviceItem") ServiceItemCreateDto createDto,
                         BindingResult bindingResult,
                         RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "admin/services/create";
        }

        adminServiceItemService.create(createDto);
        redirectAttributes.addFlashAttribute("success", "Služba byla vytvořena");
        return "redirect:/admin/services";
    }

    // --- Форма редактирования ---
    @GetMapping("/edit/{id}")
    public String editForm(@PathVariable Long id, Model model) {
        ServiceItemResponseDto service = adminServiceItemService.getById(id);
        model.addAttribute("serviceItem", service);
        return "admin/services/edit";
    }

    // --- Обновление услуги ---
    @PostMapping("/edit/{id}")
    public String update(@PathVariable Long id,
                         @Valid @ModelAttribute("serviceItem") ServiceItemUpdateDto updateDto,
                         BindingResult bindingResult,
                         RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "admin/services/edit";
        }

        adminServiceItemService.update(id, updateDto);
        redirectAttributes.addFlashAttribute("success", "Služba byla aktualizována");
        return "redirect:/admin/services";
    }

    // --- Загрузка фото ---
    @PostMapping("/{id}/photo")
    public String uploadPhoto(@PathVariable Long id,
                              @RequestParam("file") MultipartFile file,
                              RedirectAttributes redirectAttributes) {
        if (file.isEmpty()) {
            redirectAttributes.addFlashAttribute("error", "Vyberte soubor");
            return "redirect:/admin/services/edit/" + id;
        }

        adminServiceItemService.uploadPhoto(file, id);
        redirectAttributes.addFlashAttribute("success", "Foto nahráno");
        return "redirect:/admin/services/edit/" + id;
    }

    // --- Удаление фото ---
    @PostMapping("/{id}/photo/delete")
    public String deletePhoto(@PathVariable Long id,
                              RedirectAttributes redirectAttributes) {
        adminServiceItemService.deletePhoto(id);
        redirectAttributes.addFlashAttribute("success", "Foto smazáno");
        return "redirect:/admin/services/edit/" + id;
    }

    // --- Удаление услуги ---
    @PostMapping("/delete/{id}")
    public String delete(@PathVariable Long id,
                         RedirectAttributes redirectAttributes) {
        adminServiceItemService.deleteById(id);
        redirectAttributes.addFlashAttribute("success", "Služba byla smazána");
        return "redirect:/admin/services";
    }
}
