package line.business.work;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    @GetMapping("/test")
    public String testService() {
        return "Spring Boot Service is Running!";
    }
}