using System.Collections;
using System.Collections.Generic;
using UnityEngine;


[System.Serializable]
public class CraftingSkill
{

    public string Name { get; set; }
    public string KnowledgeLevel { get; set; }
    public int Level { get; set; }
    public int LevelCap { get; set; }
    public int Experience { get; set;}
    public int ExpToNext { get; set; }

    [Newtonsoft.Json.JsonConstructor]
    public CraftingSkill(string name, string knowledgeLevel, int level, int levelCap)
    {
        this.Name = name;
        this.KnowledgeLevel = knowledgeLevel;
        this.Level = level;
        this.LevelCap = levelCap;
    }

    public void LevelUp()
    {
        this.Level += 1;
    }

    public void KnowledgeUp()
    {
        if (this.Level == 1)
        {
            this.KnowledgeLevel = "Uneducated";
        }
        else if (this.Level == 25)
        {
            this.KnowledgeLevel = "Aboslute Novice";
        }
    }

    public void AddExperience()
    {

    }
}
