package academy.devdojo.domain;

import lombok.Data;
import lombok.Getter;

import java.util.List;

@Getter
public class Anime {

    private Long id;
    private String name;

    public Anime(Long id, String name) {
        this.id = id;
        this.name = name;
    }



    public static List<Anime> getAnimes() {
        var Fullmetal = new Anime(1L, "Fullmetal");
        var Cavaleiros = new Anime(2L, "Cavaleiros");
        var DragonBall = new Anime(3L, "DragonBall");

        return List.of(Fullmetal, Cavaleiros, DragonBall);
    }
}
