package exercise.daytime;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;

@Component
public class Day implements Daytime {
    private String name = "day";

    public String getName() {
        return name;
    }

    // BEGIN
    @PostConstruct
    public void init() {
        System.out.println("name Bean: " + name + " init");
    }
    // END
}
