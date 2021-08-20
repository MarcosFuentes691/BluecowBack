package com.bluecow.utility;

import com.bluecow.consts.ConstHeroes;

import java.util.ArrayList;

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

}
