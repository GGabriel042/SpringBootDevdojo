package academy.devdojo.repository;

import academy.devdojo.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class UserHardCodedRepository {

    private final UserData userData;

    public List<User> findAll() {
        return userData.getUsers();
    }

    public Optional<User> findById (Long id) {
        return userData.getUsers().stream().filter(user -> user.getId().equals(id)).findFirst();
    }

    public List<User> findByFirstName(String name) {
        return userData.getUsers().stream().filter(user -> user.getFirstName().equalsIgnoreCase(name)).toList();
    }

    public User saveUser(User user) {
        userData.getUsers().add(user);
        return user;
    }

    public void deleteUser(User user) {
        userData.getUsers().remove(user);
    }

    public void updateUser(User user) {
        System.out.println("Repositorio recebeu "+ user.getFirstName());
        deleteUser(user);
        saveUser(user);
    }
}
