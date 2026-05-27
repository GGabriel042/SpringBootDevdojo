package academy.devdojo.producer;

import academy.devdojo.domain.Producer;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Component
public class ProducerData {
    private final List<Producer> producers = new ArrayList<>();

    {
        var Mappa = Producer.builder().id(1L).name("Mappa").createdAt(LocalDateTime.now()).build();
        var KyotoAnimations = Producer.builder().id(2L).name("Kyoto Animations").createdAt(LocalDateTime.now()).build();
        var MadHouse = Producer.builder().id(3L).name("Mad House").createdAt(LocalDateTime.now()).build();
        producers.addAll(List.of(Mappa, KyotoAnimations, MadHouse));
    }

    public List<Producer> getProducers() {
        return producers;
    }
}
