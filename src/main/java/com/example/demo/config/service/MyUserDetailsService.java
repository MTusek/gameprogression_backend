package com.example.demo.config.service;

import com.example.demo.config.model.User;
import com.example.demo.config.model.UserPrincipal;
import com.example.demo.config.repo.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class MyUserDetailsService implements UserDetailsService {


    @Autowired
    private UserRepository repository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = repository.findByUsername(username);
                if(user ==  null){
                    throw new UsernameNotFoundException("User not found!");
                }
        return new UserPrincipal(user);
    }
}
