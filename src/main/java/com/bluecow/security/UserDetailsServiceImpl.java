package com.bluecow.security;

import com.bluecow.entity.Player;
import com.bluecow.service.PlayerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    PlayerService playerService;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Player player = playerService.getByEmail(email).orElseThrow(()-> new UsernameNotFoundException("email no encontrado"));
        return UserAuthFactory.build(player);
    }
}
