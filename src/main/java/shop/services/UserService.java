package shop.services;

import shop.dto.UserDto;
import shop.models.User;
public interface UserService {
    User save(UserDto userDto);
    }

