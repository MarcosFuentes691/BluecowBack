package com.bluecow.service;

import com.bluecow.entity.Player;
import com.bluecow.repository.PlayerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
public class PlayerService {

    @Autowired
    PlayerRepository playerRepository;

    public Optional<Player> getByEmail(String email){
        return playerRepository.findByEmail(email);
    }

    public boolean existsEmail(String email){
        return playerRepository.existsByEmail(email);
    }

    public Player save(Player player){
        return playerRepository.save(player);
    }
}
