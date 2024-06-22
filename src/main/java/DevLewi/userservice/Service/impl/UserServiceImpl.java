package DevLewi.userservice.Service.impl;

import DevLewi.userservice.Service.UserService;
import DevLewi.userservice.domain.Confirmation;
import DevLewi.userservice.domain.User;
import DevLewi.userservice.repository.ConfirmationRepository;
import DevLewi.userservice.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final ConfirmationRepository confirmationRepository;
    private final EmailService emailService;

    @Override
    public User saveUser(User user) {
        if (userRepository.existsByEmail(user.getEmail())) {
            throw new RuntimeException("Email already exists");
        }
        user.setEnabled(false);
        userRepository.save(user);

        Confirmation confirmation = new Confirmation(user);
        confirmationRepository.save(confirmation);

        // Assuming EmailService has a method to send confirmation email
        emailService.sendConfirmationEmail(user.getEmail(), confirmation.getToken());

        return user;
    }
}
