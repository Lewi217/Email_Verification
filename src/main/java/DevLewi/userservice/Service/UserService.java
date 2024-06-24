package DevLewi.userservice.Service;

import DevLewi.userservice.domain.User;

public interface UserService {
    User saveUser(User user);

    Boolean verifyToken(String token);
}
