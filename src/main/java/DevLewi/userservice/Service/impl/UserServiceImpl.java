package DevLewi.userservice.Service.impl;

import DevLewi.userservice.Service.EmailService;
import DevLewi.userservice.Service.UserService;
import DevLewi.userservice.domain.Confirmation;
import DevLewi.userservice.domain.User;
import DevLewi.userservice.repository.ConfirmationRepository;
import DevLewi.userservice.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

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

        emailService.sendMimeMessageWithEmbeddedFiles(user.getName(), user.getEmail(), confirmation.getToken());
        emailService.sendHtmlEmail(user.getName(), user.getEmail(), confirmation.getToken());
        return user;
    }

    @Override
    public Boolean verifyToken(String token){
        // Fetch confirmation by token
        Optional<Confirmation> optionalConfirmation = confirmationRepository.findByToken(token);

        // Check if confirmation is present
        if (!optionalConfirmation.isPresent()) {
            throw new RuntimeException("Invalid token");
        }

        Confirmation confirmation = optionalConfirmation.get();
        User user = confirmation.getUser();
        if (user == null) {
            throw new RuntimeException("No user associated with this token");
        }

        // Enable the user
        user.setEnabled(true);
        userRepository.save(user);

        // Optionally delete the confirmation record after use
        // confirmationRepository.delete(confirmation);

        return Boolean.TRUE;
    }
}
