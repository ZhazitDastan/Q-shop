package shop.security;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import shop.models.User;
import shop.repositories.UserRepository;
import shop.services.UserService;
import shop.dto.UserDto;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {


    private final PasswordEncoder passwordEncoder;

    private final UserRepository userRepository;

    @Override
    public User save(UserDto userDto) {
        User user = new User(userDto.getEmail(), passwordEncoder.encode(userDto.getPassword()) , userDto.getRole(), userDto.getFullname());
        return userRepository.save(user);
    }

}
