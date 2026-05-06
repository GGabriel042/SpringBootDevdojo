package academy.devdojo.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class Anime {

    private Long id;
    private String name;

    @Getter
    private static List<Anime> animes = new ArrayList<>();
    static {
        var Fullmetal = new Anime(1L, "Fullmetal");
        var Cavaleiros = new Anime(2L, "Cavaleiros");
        var DragonBall = new Anime(3L, "DragonBall");
        animes.addAll(List.of(Fullmetal, Cavaleiros, DragonBall));
    }


}
