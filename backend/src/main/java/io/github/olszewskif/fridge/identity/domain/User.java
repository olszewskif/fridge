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
    private String hashedPassword;
    private UserRole role;

    private User(UserId id, String displayName, Email email, String password, UserRole role) {
        super(id);
        this.displayName = DomainUtil.notBlank(displayName, "Display name cannot be null or blank!");
        this.email = DomainUtil.notNull(email, "Email cannot be null!");
        this.hashedPassword = DomainUtil.notBlank(password, "Password cannot be null or blank!");
        this.role = DomainUtil.notNull(role, "Role cannot be null!");
    }

    private User(UserId id, String displayName, Email email, String password,
                 UserRole role, UniqueEmailChecker emailChecker) {
        this(id, displayName, email, password, role);
        if (!emailChecker.isUnique(email)) {
            throw new EmailAlreadyExistsException(email);
        }
    }

    public static User create(String displayName, Email email, String password,
                              UniqueEmailChecker emailChecker) {
        return new User(UserId.generate(), displayName, email, password, UserRole.USER, emailChecker);
    }

    public static User createWithRole(String displayName, Email email, String password,
                                      UserRole role, UniqueEmailChecker emailChecker) {
        return new User(UserId.generate(), displayName, email, password, role, emailChecker);
    }

    public static User restore(UserId id, String displayName, Email email, String password,
                               UserRole role) {
        return new User(id, displayName, email, password, role);
    }

    @Nonnull public String getDisplayName() {
        return displayName;
    }

    @Nonnull public Email getEmail() {
        return email;
    }

    @Nonnull public String getPassword() {
        return hashedPassword;
    }

    @Nonnull public UserRole getRole() {
        return role;
    }

    public void changeDisplayName(String displayName) {
        this.displayName = DomainUtil.notBlank(displayName, "Username cannot be null or blank!");
    }

    public void changeEmail(Email email) {
        this.email = DomainUtil.notNull(email, "Email cannot be null!");
    }

    public void changePassword(String hashedPassword) {
        this.hashedPassword = DomainUtil.notBlank(hashedPassword, "Password cannot be null or blank!");
    }

    public void changeRole(UserRole newRole) {
        this.role = DomainUtil.notNull(newRole, "Role cannot be null!");
    }
}
