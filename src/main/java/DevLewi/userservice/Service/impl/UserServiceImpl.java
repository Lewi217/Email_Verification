package DevLewi.userservice.Service.impl;

import DevLewi.userservice.Service.EmailService;
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

       /* TODO Send email to user with token*/
        emailService.sendSimpleMailMessage(user.getName(), user.getEmail(), confirmation.getToken());

        return user;
    }

    @Override
    public Boolean verifyToken(String token){
        Confirmation confirmation = confirmationRepository.findByToken(token);
        User user = userRepository.findByEmailIgnoreCase(confirmation.getUser().getEmail());
        user.setEnabled(true);
        userRepository.save(user);
       // confirmationRepository.delete(confirmation);
        return Boolean.TRUE;
    }
}
