package academy.devdojo.commons;

import academy.devdojo.domain.User;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class UserUtils {

    public List<User> newUserList() {
        User gabriel = User.builder()
                .id(1L)
                .firstName("Gabriel")
                .lastName("Gonzaga")
                .email("GG@gmail.com")
                .roles("USER")
                .password("{bcrypt}$2a$10$x4ykW1ZRrJTxtkS2Zv58DerRTbNeIz9fLms3Bo1y7L3axkGwYa5zW")
                .build();
        User rafael = User.builder()
                .id(2L)
                .firstName("Rafael")
                .lastName("Gonzaga")
                .email("RG@gmail.com")
                .roles("USER")
                .password("{bcrypt}$2a$10$x4ykW1ZRrJTxtkS2Zv58DerRTbNeIz9fLms3Bo1y7L3axkGwYa5zW")
                .build();
        User daniel = User.builder()
                .id(3L)
                .firstName("Daniel")
                .lastName("Gonzaga")
                .email("DG@gmail.com")
                .roles("USER")
                .password("{bcrypt}$2a$10$x4ykW1ZRrJTxtkS2Zv58DerRTbNeIz9fLms3Bo1y7L3axkGwYa5zW")
                .build();
        return new ArrayList<>(List.of(gabriel, rafael, daniel));
    }

    public User newUserToSave() {
        return User.builder()
                .firstName("Rodolfo")
                .lastName("Afonso")
                .email("Rodolfonso@teste.com")
                .roles("USER")
                .password("{bcrypt}$2a$10$x4ykW1ZRrJTxtkS2Zv58DerRTbNeIz9fLms3Bo1y7L3axkGwYa5zW")
                .build();
    }

    public User newUserSaved() {
        return User.builder()
                .id(99L)
                .firstName("Rodolfo")
                .lastName("Afonso")
                .email("Rodolfonso@teste.com")
                .roles("USER")
                .password("{bcrypt}$2a$10$x4ykW1ZRrJTxtkS2Zv58DerRTbNeIz9fLms3Bo1y7L3axkGwYa5zW")
                .build();
    }
}
