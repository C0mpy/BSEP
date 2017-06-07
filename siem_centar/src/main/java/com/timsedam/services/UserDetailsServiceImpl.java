package com.timsedam.services;

import com.timsedam.models.Permission;
import com.timsedam.models.Role;
import com.timsedam.models.User;
import com.timsedam.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Service
// nasa implementacija interfejsa za dobavljanje informacija o korisniku
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(username);
        if(user == null)
            throw new UsernameNotFoundException(String.format("No user found with username %s", username));
        else {
            // dobavljamo sve permisije koje jedan korisnik ima na osnovu njegove role
            List<GrantedAuthority> grantedAuthorities = new ArrayList<GrantedAuthority>();
            for(Permission p : user.getRole().getPermissions()) {
                grantedAuthorities.add(new SimpleGrantedAuthority(p.getName()));
            }
            // vraca se User objekat, implementacija UserDetails interfejsa
            return new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPassword(),
                    grantedAuthorities);
        }
    }
}
