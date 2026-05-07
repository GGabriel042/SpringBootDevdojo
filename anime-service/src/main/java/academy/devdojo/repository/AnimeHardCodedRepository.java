package academy.devdojo.repository;

import academy.devdojo.domain.Anime;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class AnimeHardCodedRepository {

    private static final List<Anime> ANIMES = new ArrayList<>();
    static {
        var Fullmetal = new Anime(1L, "Fullmetal");
        var Cavaleiros = new Anime(2L, "Cavaleiros");
        var DragonBall = new Anime(3L, "DragonBall");
        ANIMES.addAll(List.of(Fullmetal, Cavaleiros, DragonBall));
    }

    public List<Anime> findAll() {
        return ANIMES;
    }

    public Optional<Anime> findById(Long id) {
        return ANIMES.stream().filter(anime -> anime.getId().equals(id)).findFirst();
    }

    public List<Anime> findByName(String name) {
        return ANIMES.stream().filter(anime -> anime.getName().equalsIgnoreCase(name)).toList();
    }

    public Anime save (Anime anime) {
        ANIMES.add(anime);
        return anime;
    }

    public void delete (Anime anime) {
        ANIMES.remove(anime);
    }

    public void update (Anime anime) {
        delete(anime);
        save(anime);
    }
}
