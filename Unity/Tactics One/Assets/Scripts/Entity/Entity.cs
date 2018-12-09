using System.Collections;
using System.Collections.Generic;
using UnityEngine;

public class Entity
{
    public string Name { get; set; }
    public int Health { get; set; }
    public int MaxHealth { get; set; }
    public int Mana { get; set; }
    public int Level { get; set; }
    public bool Selected { get; set; }
    public List<Sprite> Sprite { get; set; }

    public Entity(string name, int health, int maxHealth, int mana, int level, bool selected, List<Sprite> sprite)
    {
        this.Name = name;
        this.Health = health;
        this.MaxHealth = maxHealth;
        this.Mana = mana;
        this.Level = level;
        this.Sprite = new List<Sprite>(sprite);
    }

}
