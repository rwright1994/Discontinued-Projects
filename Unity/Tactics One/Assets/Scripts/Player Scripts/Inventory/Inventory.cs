using System.Collections;
using System.Collections.Generic;
using UnityEngine;

public class Inventory : MonoBehaviour {


    [SerializeField] List<Item> items;
    [SerializeField] Transform itemParent;
    [SerializeField] ItemSlot[] itemSlots;

    

    private void OnValidate()
    {
        if(itemParent != null)
        {
            itemSlots = itemParent.GetComponentsInChildren<ItemSlot>();   
        }
      
            RefreshUI();
        
    }

    public void AddItem(Item item)
    {
        Debug.Log(items);
        
        items.Add(item);       
    }

    public void RemoveItem(Item item)
    {

    }

    private void RefreshUI()
    {
        int i = 0;
        for (; i < items.Count && i < itemSlots.Length; i++)
        {
            itemSlots[i].Item = items[i];
        }
        
        for(; i < itemSlots.Length; i++)
        {
            itemSlots[i].Item = null;
        }
    }

    public void SetInventoryOnLoad(PlayerInventory inv)
    {
        this.items = inv.items;
    }
   
    public List<Item> GetItems()
    {
       return items;
    }

}
