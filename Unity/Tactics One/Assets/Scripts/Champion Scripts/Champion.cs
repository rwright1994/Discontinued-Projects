using System.Collections;
using System.Collections.Generic;
using UnityEngine;

[System.Serializable]
public class Champion
{

    public string Name { get; set; }
    public int Health { get; set; }
    public int MaxHealth { get; set; }
    public Resource Resource { get; set; }
    public string ClassType { get; set; }
    public List<BaseStat> Stats { get; set; }
    public List<BaseStat> StatsUp { get; set; }
    public int Damage { get; set; }
    public string SpriteSlug { get; set; }
    public int Level { get; set; }
    public int Experience { get; set; }
    public int ExpToNext { get; set; }
    public bool Selected { get; set; }
    public List<CombatSkill> Skills { get; set; }
    

    [Newtonsoft.Json.JsonConstructor]
    public Champion(string name, int health,  string classType, List<BaseStat> stats, List<BaseStat> statsUp, Resource resource, 
                    string spriteSlug, int level, int experience)
    {
        this.Name = name;
        this.ClassType = classType;
        this.Health = health;
        this.MaxHealth = health;
        this.Resource = resource;
        this.Stats = stats;
        this.StatsUp = statsUp;
        this.SpriteSlug = spriteSlug;
        this.Level = level;
        this.Experience = experience;
        this.Selected = false;
        this.ExpToNext = 100;
        this.Skills = null;
    }



    public void LevelUp()
    {
        this.Experience = this.Experience - this.ExpToNext;
        this.Level += 1;
        SetNewExp();
        Debug.Log(Stats.Count);
        
    }

    public void Animate()
    {
       
    }

    public void Attack(Entity target)
    {
        Damage = DetermineDamage();
        target.Health -= Damage; 
    }

    public int DetermineDamage()
    {
        Damage = 20 + (GetStatValue("Strength"));
        Debug.Log(Damage);
        return Damage;
    }



    //add skill point for player to spend
    public void AddSkillPoint()
    {

    }

    //add stat points for the player to spend.
    public void AddStatPoint()
    {

    }


    public void AddExperience(int amount)
    {
        this.Experience += amount;
    }

    public void SetNewExp()
    {
        
        this.ExpToNext = (int) (100 * Level * Mathf.Pow(1.15f, Level));
    }

    public int GetStatValue(string name)
    {
        for(int i = 0; i < Stats.Count; i++)
        {
            if(name == Stats[i].Name)
            {
                return Stats[i].Value;
            }
        }
        return 0;
    }


    public void LearnSkill(string skillName)
    {
        for(int i = 0; i < Skills.Count; i++)
        {
            if (skillName == Skills[i].GetName())
            {
                Skills[i].learned = true;
                return;
            }
        }
    }
}