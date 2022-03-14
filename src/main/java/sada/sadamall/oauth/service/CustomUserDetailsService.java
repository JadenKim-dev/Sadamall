package sada.sadamall.oauth.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import sada.sadamall.api.entity.user.User;
import sada.sadamall.api.repository.user.UserRepository;
import sada.sadamall.oauth.entity.UserPrincipal;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByOauthId(username);
        if(user == null) {
            throw new UsernameNotFoundException("Cannot find oauth id.");
        }
        return UserPrincipal.from(user);
    }
}
