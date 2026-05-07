package academy.devdojo.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Builder
public class Producer {

    private Long id;
    private String name;
    private LocalDateTime createdAt;

    @Getter
    private static List<Producer> producers = new ArrayList<>();
    static {
        var Mappa = Producer.builder().id(1L).name("Mappa").createdAt(LocalDateTime.now()).build();
        var KyotoAnimations = Producer.builder().id(2L).name("Kyoto Animations").createdAt(LocalDateTime.now()).build();
        var MadHouse = Producer.builder().id(3L).name("Mad House").createdAt(LocalDateTime.now()).build();
        producers.addAll(List.of(Mappa, KyotoAnimations, MadHouse));
    }
}
