package com.szinton.safepass.view;

import com.szinton.safepass.dto.UserDto;
import com.szinton.safepass.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;

@Controller
@RequiredArgsConstructor
public class ApplicationController {

    private final UserService userService;

    @GetMapping("/login")
    public String getLoginView() {
        return "login";
    }

    @GetMapping("/login-error")
    public String getLoginErrorView(Model model) {
        model.addAttribute("loginError", true);
        return "login";
    }

    @GetMapping("/register")
    public String getRegisterView(Model model) {
        model.addAttribute("userDto", new UserDto());
        return "register";
    }

    @PostMapping("/register")
    public String postRegister(@Valid @ModelAttribute UserDto userDto, BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("userDto", userDto);
            return "register";
        }
        userService.registerUser(userDto);
        return "redirect:/master-password";
    }

    @GetMapping("/master-password")
    public String getMasterPasswordView() {
        return "master-password";
    }
}
