﻿using System.Collections;
using System.Collections.Generic;
using UnityEngine;


[CreateAssetMenu]
public class Item : ScriptableObject {

    public new string name;
    public int LevelRequirement;
    public Sprite sprite;
}
