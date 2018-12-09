using System.Collections;
using System.Collections.Generic;
using UnityEngine;


[CreateAssetMenu(fileName = "New Monster", menuName = "Monster")]
public class Enemy : ScriptableObject
{


    public new string name;
    public int health;
    public int maxHealth;
    public int mana;
    public int level;
    public bool selected;

    public Brain enemyBrain;
    public List<Sprite> sprites;


}


