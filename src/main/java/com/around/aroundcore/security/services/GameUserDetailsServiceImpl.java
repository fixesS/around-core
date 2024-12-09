package com.around.aroundcore.security.services;

import com.around.aroundcore.database.repositories.user.GameUserRepository;
import com.around.aroundcore.core.exceptions.api.entity.GameUserNullException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public class GameUserDetailsServiceImpl implements UserDetailsService {
    private GameUserRepository userRepository;

    public GameUserDetailsServiceImpl(GameUserRepository userRepository){
        this.userRepository = userRepository;
    }
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByEmail(username)
                .orElseThrow(GameUserNullException::new);
    }

}
