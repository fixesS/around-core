package com.around.aroundcore.security;

import com.around.aroundcore.database.repositories.GameUserRepository;
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
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }

}
