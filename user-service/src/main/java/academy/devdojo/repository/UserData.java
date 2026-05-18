package academy.devdojo.repository;

import academy.devdojo.domain.User;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class UserData {

    private final List<User> users = new ArrayList<>();

    {
        User gabriel = User.builder().id(1L).firstName("Gabriel").lastName("Gonzaga").email("GG@gmail.com").build();
        User rafael = User.builder().id(2L).firstName("Rafael").lastName("Gonzaga").email("RG@gmail.com").build();
        User daniel = User.builder().id(3L).firstName("Daniel").lastName("Gonzaga").email("DG@gmail.com").build();
        users.addAll(List.of(gabriel, rafael, daniel));
    }

    public List<User> getUsers() {
        return users;
    }
}
