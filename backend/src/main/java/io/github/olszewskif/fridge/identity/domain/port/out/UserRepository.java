package io.github.olszewskif.fridge.identity.domain.port.out;

import io.github.olszewskif.fridge.identity.domain.User;
import io.github.olszewskif.fridge.identity.domain.UserId;

import java.util.Optional;

public interface UserRepository {
    User save(User user);
    Optional<User> findById(UserId id);
}
