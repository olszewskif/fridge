package io.github.olszewskif.fridge.identity.domain;

import io.github.olszewskif.fridge.identity.domain.exception.EmailAlreadyExistsException;
import io.github.olszewskif.fridge.identity.domain.port.out.UniqueEmailChecker;
import io.github.olszewskif.fridge.shared.domain.AggregateRoot;
import io.github.olszewskif.fridge.shared.domain.DomainUtil;
import jakarta.annotation.Nonnull;

import java.util.Objects;

public class User extends AggregateRoot<UserId> {

    private String displayName;
    private Email email;
    private String password;

    private User(UserId id, String displayName, Email email, String password) {
        super(id);
        this.displayName = DomainUtil.notBlank(displayName, "Display name cannot be null or blank!");
        this.email = Objects.requireNonNull(email, "Email cannot be null!");
        this.password = DomainUtil.notBlank(password, "Password cannot be null or blank!");
    }

    private User(UserId id, String displayName, Email email, String password,
                 UniqueEmailChecker emailChecker) {
        this(id, displayName, email, password);
        if (!emailChecker.isUnique(email)) {
            throw new EmailAlreadyExistsException(email);
        }
    }

    public static User create(String displayName, Email email, String password,
                              UniqueEmailChecker emailChecker) {
        return new User(UserId.generate(), displayName, email, password, emailChecker);
    }

    public static User restore(UserId id, String displayName, Email email, String password) {
        return new User(id, displayName, email, password);
    }

    @Nonnull public String getDisplayName() {
        return displayName;
    }

    @Nonnull public Email getEmail() {
        return email;
    }

    @Nonnull public String getPassword() {
        return password;
    }

    public void setDisplayName(String displayName) {
        this.displayName = DomainUtil.notBlank(displayName, "Username cannot be null or blank!");
    }

    public void setEmail(Email email) {
        this.email = Objects.requireNonNull(email, "Email cannot be null!");
    }

    public void setPassword(String password) {
        this.password = DomainUtil.notBlank(password, "Password cannot be null or blank!");
    }
}
