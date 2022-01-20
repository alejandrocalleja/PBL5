package eus.blankcard.decklearn.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import eus.blankcard.decklearn.models.UserModel;
import eus.blankcard.decklearn.repository.user.UserRepository;


@Service
public class UserService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserModel user = userRepository.findByUsername(username);

        if (user == null) {
            throw new UsernameNotFoundException("User " + username + " not found.");
        }

        List<GrantedAuthority> roles = new ArrayList<>();
        roles.add(new SimpleGrantedAuthority("USER"));
        UserDetails userDet = new User(user.getUsername(), user.getPassword(), roles);

        return userDet;
    }

    // @Override
    // public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    //   UserModel user = userRepository.findByUsername(username);
    //   UserBuilder builder = null;

    //   if (user != null) {
    //     builder = org.springframework.security.core.userdetails.User.withUsername(username);
    //     builder.username(user.getUsername());
    //     builder.password(user.getPassword());
        
    //     List<GrantedAuthority> roles = new ArrayList<>();
    //     roles.add(new SimpleGrantedAuthority("USER"));
    //     builder.roles((String[]) roles.toArray());

    //   } else {
    //     throw new UsernameNotFoundException("User not found.");
    //   }

    //   return builder.build();
    // }
}