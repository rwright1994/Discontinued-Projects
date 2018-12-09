using System.Collections;
using System.Collections.Generic;
using UnityEngine;

[CreateAssetMenu(fileName = "Skills", menuName = "Combat Skill")]
public class CombatSkill : ScriptableObject {

    public new string name;
    public int level;
    public int levelReq;
    public string classReq;
    public int damage;
    public double multipler;
    public int cost;
    public bool learned;
    public int cooldownTime;
    public int cooldown;

    public void AddLevel()
    {
        this.level += 1;
    }

    public void Use()
    {

    }

    public string GetName()
    {
        return this.name;
    }


    



}
