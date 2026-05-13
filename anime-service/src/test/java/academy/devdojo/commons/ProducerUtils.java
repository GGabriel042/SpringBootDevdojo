package academy.devdojo.commons;

import academy.devdojo.domain.Producer;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Component
public class ProducerUtils {

    public List<Producer> newProducerList() {
        var dateTime = "2026-05-12T18:14:01.764901";
        var formatted = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSSSS");
        var localDateTime = LocalDateTime.parse(dateTime, formatted);

        var ufotable = Producer.builder().id(1L).name("ufotable").createdAt(localDateTime).build();
        var witStudio = Producer.builder().id(2L).name("witStudio").createdAt(localDateTime).build();
        var studioGhibli = Producer.builder().id(3L).name("studioGhibli").createdAt(localDateTime).build();

        return new ArrayList<>(List.of(ufotable, witStudio, studioGhibli));
    }
}
