package academy.devdojo.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class Producer {

    private Long id;
    private String name;

    @Getter
    private static List<Producer> producers = new ArrayList<>();
    static {
        var Mappa = new Producer(1L, "Mappa");
        var KyotoAnimations = new Producer(2L, "Kyoto Animations");
        var MadHouse = new Producer(3L, "Mad House");
        producers.addAll(List.of(Mappa, KyotoAnimations, MadHouse));
    }
}
