package line.business.work.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    @GetMapping("/selfping")
    public String testService() {
        return "Spring Boot Service is Running!";
    }
}