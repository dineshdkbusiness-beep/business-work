package line.business.work.component;


import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class SchedulerService {

    private final RestTemplate restTemplate = new RestTemplate();

    @Scheduled(fixedRate = 840000) 
    public void keepServerAlive() {

        String url = "https://business-work-1.onrender.com/selfping";

        try {
            String response = restTemplate.getForObject(url, String.class);
            System.out.println("Self Ping Response: " + response);
        } catch (Exception e) {
            System.out.println("Self Ping Failed: " + e.getMessage());
        }
    }
}
