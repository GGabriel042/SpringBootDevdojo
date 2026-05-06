package academy.devdojo.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class Anime {

    private Long id;
    private String name;


    public static List<Anime> getAnimes() {
        var Fullmetal = new Anime(1L, "Fullmetal");
        var Cavaleiros = new Anime(2L, "Cavaleiros");
        var DragonBall = new Anime(3L, "DragonBall");

        return List.of(Fullmetal, Cavaleiros, DragonBall);
    }
}
