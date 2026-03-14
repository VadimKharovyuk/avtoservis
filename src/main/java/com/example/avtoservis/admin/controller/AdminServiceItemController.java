package com.example.avtoservis.admin.controller;

import com.example.avtoservis.admin.service.AdminServiceItemService;
import com.example.avtoservis.dto.ServiceItemCreateDto;
import com.example.avtoservis.dto.ServiceItemResponseDto;
import com.example.avtoservis.dto.ServiceItemTranslation.ServiceItemTranslationDto;
import com.example.avtoservis.dto.ServiceItemUpdateDto;
import com.example.avtoservis.enums.Language;
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

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/admin/services")
@RequiredArgsConstructor
public class AdminServiceItemController {

    private final AdminServiceItemService adminServiceItemService;

    // --- Список послуг ---
    @GetMapping
    public String list(Pageable pageable, Model model) {
        PageResponse<ServiceItemResponseDto> page = adminServiceItemService.getAll(pageable);
        model.addAttribute("services", page);
        return "admin/services/list";
    }

    // --- Форма створення ---
    @GetMapping("/create")
    public String createForm(Model model) {
        ServiceItemCreateDto dto = new ServiceItemCreateDto();

        // Створюємо порожні переклади для кожної мови
        List<ServiceItemTranslationDto> translations = Language.getEnabledLanguages().stream()
                .map(lang -> ServiceItemTranslationDto.builder()
                        .language(lang)
                        .build())
                .collect(Collectors.toCollection(ArrayList::new));
        dto.setTranslations(translations);

        model.addAttribute("serviceItem", dto);
        model.addAttribute("languages", Language.getEnabledLanguages());
        return "admin/services/create";
    }

    // --- Збереження нової послуги ---

// === В контролері AdminServiceItemController — метод create ===
@PostMapping("/create")
public String create(@Valid @ModelAttribute("serviceItem") ServiceItemCreateDto createDto,
                     BindingResult bindingResult,
                     Model model,
                     RedirectAttributes redirectAttributes) {

    System.out.println("=== STEP 1: DATA RECEIVED ===");
    System.out.println("Price: " + createDto.getPrice());
    System.out.println("Category: " + createDto.getCategory());
    System.out.println("Active: " + createDto.isActive());
    System.out.println("PriceFrom: " + createDto.isPriceFrom());
    System.out.println("Translations: " + (createDto.getTranslations() != null ? createDto.getTranslations().size() : "NULL"));
    System.out.println("Active from form: " + createDto.isActive());

    if (createDto.getTranslations() != null) {
        for (int i = 0; i < createDto.getTranslations().size(); i++) {
            var t = createDto.getTranslations().get(i);
            System.out.println("  [" + i + "] lang=" + t.getLanguage()
                    + " | name='" + t.getName() + "'"
                    + " | slug='" + t.getSlug() + "'"
                    + " | desc='" + t.getDescription() + "'");
        }
    }

    System.out.println("=== STEP 2: BINDING ERRORS ===");
    if (bindingResult.hasErrors()) {
        bindingResult.getAllErrors().forEach(e ->
                System.out.println("  ERROR: field=" + e.getObjectName() + " msg=" + e.getDefaultMessage()));
        bindingResult.getFieldErrors().forEach(e ->
                System.out.println("  FIELD ERROR: " + e.getField() + " = " + e.getDefaultMessage() + " (rejected=" + e.getRejectedValue() + ")"));
    } else {
        System.out.println("  No binding errors");
    }

    // CS перевірка
    boolean csNameMissing = createDto.getTranslations().stream()
            .filter(t -> t.getLanguage() == Language.CS)
            .anyMatch(t -> t.getName() == null || t.getName().isBlank());
    System.out.println("=== STEP 3: CS name missing = " + csNameMissing + " ===");

    if (csNameMissing) {
        bindingResult.rejectValue("translations[0].name", "", "Čeština nazva je povinná");
    }

    if (bindingResult.hasErrors()) {
        System.out.println("=== STEP 4: RETURNING TO FORM (errors exist) ===");
        model.addAttribute("languages", Language.getEnabledLanguages());
        return "admin/services/create";
    }

    System.out.println("=== STEP 5: CALLING SERVICE ===");
    try {
        adminServiceItemService.create(createDto);
        System.out.println("=== STEP 6: SUCCESS ===");
        redirectAttributes.addFlashAttribute("success", "Послугу створено");
        return "redirect:/admin/services";
    } catch (Exception e) {
        System.out.println("=== STEP 6: EXCEPTION ===");
        System.out.println("  Type: " + e.getClass().getName());
        System.out.println("  Message: " + e.getMessage());
        e.printStackTrace();
        model.addAttribute("error", "Помилка: " + e.getMessage());
        model.addAttribute("languages", Language.getEnabledLanguages());
        return "admin/services/create";
    }
}

    @GetMapping("/edit/{id}")
    public String editForm(@PathVariable Long id, Model model) {
        ServiceItemResponseDto service = adminServiceItemService.getById(id);

        // Впорядковуємо переклади по порядку мов
        List<Language> languages = Language.getEnabledLanguages();
        List<ServiceItemTranslationDto> orderedTranslations = new ArrayList<>();

        for (Language lang : languages) {
            ServiceItemTranslationDto found = service.getTranslations().stream()
                    .filter(t -> t.getLanguage() == lang)
                    .findFirst()
                    .orElse(ServiceItemTranslationDto.builder()
                            .language(lang)
                            .build());
            orderedTranslations.add(found);
        }
        service.setTranslations(orderedTranslations);

        model.addAttribute("serviceItem", service);
        model.addAttribute("languages", languages);
        return "admin/services/edit";
    }

    // --- Оновлення послуги ---
    @PostMapping("/edit/{id}")
    public String update(@PathVariable Long id,
                         @Valid @ModelAttribute("serviceItem") ServiceItemUpdateDto updateDto,
                         BindingResult bindingResult,
                         Model model,
                         RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("languages", Language.getEnabledLanguages());
            return "admin/services/edit";
        }

        adminServiceItemService.update(id, updateDto);
        redirectAttributes.addFlashAttribute("success", "Послугу оновлено");
        return "redirect:/admin/services";
    }

    // --- Завантаження фото ---
    @PostMapping("/{id}/photo")
    public String uploadPhoto(@PathVariable Long id,
                              @RequestParam("file") MultipartFile file,
                              RedirectAttributes redirectAttributes) {
        if (file.isEmpty()) {
            redirectAttributes.addFlashAttribute("error", "Оберіть файл");
            return "redirect:/admin/services/edit/" + id;
        }

        adminServiceItemService.uploadPhoto(file, id);
        redirectAttributes.addFlashAttribute("success", "Фото завантажено");
        return "redirect:/admin/services/edit/" + id;
    }

    // --- Видалення фото ---
    @PostMapping("/{id}/photo/delete")
    public String deletePhoto(@PathVariable Long id,
                              RedirectAttributes redirectAttributes) {
        adminServiceItemService.deletePhoto(id);
        redirectAttributes.addFlashAttribute("success", "Фото видалено");
        return "redirect:/admin/services/edit/" + id;
    }

    // --- Видалення послуги ---
    @PostMapping("/delete/{id}")
    public String delete(@PathVariable Long id,
                         RedirectAttributes redirectAttributes) {
        adminServiceItemService.deleteById(id);
        redirectAttributes.addFlashAttribute("success", "Послугу видалено");
        return "redirect:/admin/services";
    }
}