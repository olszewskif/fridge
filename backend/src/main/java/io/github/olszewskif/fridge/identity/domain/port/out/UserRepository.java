package io.github.olszewskif.fridge.identity.domain.port.out;

import io.github.olszewskif.fridge.identity.domain.User;

public interface UserRepository {
    User save(User user);
}
