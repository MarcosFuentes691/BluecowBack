package com.bluecow.security;

import com.bluecow.entity.Player;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.List;
import java.util.stream.Collectors;

public class UserAuthFactory {

    public static UserAuth build(Player player){
        List<GrantedAuthority> authorities =
                player.getRoles().stream().map(role -> new SimpleGrantedAuthority(role.getRoleName().name())).collect(Collectors.toList());
        return new UserAuth(player.getEmail(), player.getPassword(), authorities);
    }
}
