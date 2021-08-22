package com.bluecow.utility;

import com.bluecow.consts.ConstHeroes;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

@Slf4j
@Component
public class HeroUtility {

    ArrayList<String> heroes = ConstHeroes.heroList;

    public boolean heroExists(String heroName){
        boolean present=false;
        for (String hero : heroes) {
            if (heroName.equals(hero)) {
                present = true;
                break;
            }
        }
        return present;
    }

    public int heroesAmount(){
        return heroes.size();
    }

}
