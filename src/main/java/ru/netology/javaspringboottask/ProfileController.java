package ru.netology.javaspringboottask;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/")
public class ProfileController {

    private static final Logger log = LoggerFactory.getLogger(ProfileController.class);

    private final SystemProfile profile;

    public ProfileController(SystemProfile profile) {
        this.profile = profile;
    }

    @GetMapping("profile")
    public String getProfile() {
        log.info("Получен запрос на /profile, профиль: {}", profile.getProfile());
        return profile.getProfile();
    }
}

