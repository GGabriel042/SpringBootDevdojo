package academy.devdojo.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("v1/heroes")
public class HeroController {

    private static final List<String> Heroes = java.util.List.of("Guts", "Zoro", "Kakashi", "Goku");

    @GetMapping
    public List<String> listAllHeroes() {
        return Heroes;
    }

    @GetMapping("filter")
    public List<String> ListAllHeroesParam(@RequestParam (defaultValue = "") String name) {
        return Heroes.stream().filter(hero -> hero.equalsIgnoreCase(name)).toList();
    }

    @GetMapping("filterList")
    public List<String> ListAllHeroesParamList(@RequestParam List<String> name) {
        return Heroes.stream().filter(name::contains).toList();
    }
}
