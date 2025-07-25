package pt.ul.fc.css.soccernow.controllers;

import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MenuController {

    @GetMapping("/menu")
    public String menu(HttpSession session, Model model) {
        String user = (String) session.getAttribute("user");
        model.addAttribute("user", user != null ? user : "Utilizador");
        return "menu";
    }
}