using System.Collections;
using System.Collections.Generic;
using UnityEngine;


public enum EquipmentType {
    Chest,
    Weapon1,
    Weapon2,
    Helmet,
    Shoulder,
    Belt,
    Boots,
    Ring1,
    Ring2,
    Amulet,
    }
[CreateAssetMenu]
public class EquipableItem : Item {

    public int FlatBonus;
    public int PercentBonus;
    public int Prefixes;
    public int Suffixes;
    public EquipmentType EquipmentType;
}
