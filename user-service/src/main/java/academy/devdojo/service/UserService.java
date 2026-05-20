package academy.devdojo.service;

import academy.devdojo.domain.User;
import academy.devdojo.exception.NotFoundException;
import academy.devdojo.repository.UserHardCodedRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserHardCodedRepository repository;

    public List<User> findAll(String name) {
        return name == null ? repository.findAll() : repository.findByFirstName(name);
    }

    public User findByIdOrThrowNotFound(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new NotFoundException("User not found"));
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
