package com.web.project.dto.TimeLine;

import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class ChampionStats{
    private int abilityHaste;
    private int abilityPower;
    private int armor;
    private int armorPen;
    private int armorPenPercent;
    private int attackDamage;
    private int attackSpeed;
    private int bonusArmorPenPercent;
    private int bonusMagicPenPercent;
    private int ccReduction;
    private int cooldownReduction;
    private int health;
    private int healthMax;
    private int healthRegen;
    private int lifesteal;
    private int magicPen;
    private int magicPenPercent;
    private int magicResist;
    private int movementSpeed;
    private int omnivamp;
    private int physicalVamp;
    private int power;
    private int powerMax;
    private int powerRegen;
    private int spellVamp;
}