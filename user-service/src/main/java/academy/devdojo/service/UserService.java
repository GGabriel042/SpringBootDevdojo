package academy.devdojo.service;

import academy.devdojo.domain.User;
import academy.devdojo.repository.UserHardcoreRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserHardcoreRepository repository;

    public List<User> findAll(String name) {
        return name == null ? repository.findAll() : repository.findByFirstName(name);
    }

    public User findByIdOrThrowNotFound(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
    }

    public User saveUser (User user) {
        return repository.saveUser(user);
    }

    public void deleteUser (Long id) {
        var userToDelete = findByIdOrThrowNotFound(id);
        repository.deleteUser(userToDelete);
    }

    public void updateUser (User userToUpdate) {
        findByIdOrThrowNotFound(userToUpdate.getId());
        repository.updateUser(userToUpdate);
    }
}
