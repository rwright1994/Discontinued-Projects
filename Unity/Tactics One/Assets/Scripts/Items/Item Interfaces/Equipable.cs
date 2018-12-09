using System.Collections;
using System.Collections.Generic;
using UnityEngine;

interface IEquipable
{

    void Equip(Champion champ);
    void Unequip(Champion champ);
    void AddStats(Champion champ);
}