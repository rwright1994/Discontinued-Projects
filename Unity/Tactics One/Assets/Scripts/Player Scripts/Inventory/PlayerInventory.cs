using System.Collections;
using System.Collections.Generic;
using UnityEngine;

[System.Serializable]
public class PlayerInventory
{

    public List<Item> items;
    public int Size;
    

    public PlayerInventory(int size)
    {
        this.items = new List<Item>();
        this.Size = size;
    }

    public void AddItem(Item item)
    {
        if(items.Count < Size)
        {
            items.Add(item);
            
            return;
        }

        Debug.Log("No Item Added, Inventory full.");
    }

    public void RemoveItem(Item item)
    {
        for(int i = 0; i < items.Count; i++)
        {
            if(item.name == items[i].name && items[i] != null)
            {
                items[i] = null;
                return;
            }
        }
        Debug.Log(item.name + " is not in the inventory.");
    }

  
    }
