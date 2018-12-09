using System.Collections;
using System.Collections.Generic;
using UnityEngine;

[System.Serializable]
public class Player{

    public List<Champion> Champions { get; set; }
    public bool Alive { get; set; }
    public int ChampionMax;
    public Champion SelectedChampion { get; set; }
    public GameObject SelectedObj { get; set; }
    public Entity Target { get; set; }
    public List<CraftingSkill> CraftingSkills { get; set; }
    public PlayerInventory Inventory { get; set; }

   
    public Player(bool alive, int championMax)
    {
        this.Alive = alive;
        this.ChampionMax = championMax;
        this.Champions = new List<Champion>();
        this.CraftingSkills = new List<CraftingSkill>();
        this.Inventory = new PlayerInventory(25);

    }

    public void addChampion(Champion champion)
    {
        if (Champions.Count > ChampionMax)
        {
            Debug.Log("Cannot have any more Champions in your entourage");
        }
        else
        {
            Champions.Add(champion);
        }
 
    }

    public Champion GetChampions(int index)
    {
            return Champions[index];
    }

    public Champion FindChampion(string name)
    {
        
        foreach(Champion champ in Champions)
        {
            if(champ.Name == name)
            {
                return champ;
               
            }
           
        }
        return null;
    }

    public void LearnCraftingSkill(CraftingSkill craftSkill)
    {
        CraftingSkills.Add(craftSkill);
    }

    public void RemoveCraftingSkill(string str)
    {
        int pos = 0;
        foreach(CraftingSkill skill in CraftingSkills)
        {
            
            if(skill.Name == str)
            {
                CraftingSkills.Remove(skill);
            }
            else
            {
                pos++;
            }
        }
    }
}



