using System.Collections;
using System.Collections.Generic;
using UnityEngine;

public class InventoryManager : MonoBehaviour {

    private PlayerManager player;
    [SerializeField] Transform itemParent;
    [SerializeField] ItemSlot[] itemSlots;
    public ItemDatabase itemDB;

    private void OnValidate()
    {
        if (itemParent != null)
        {
            itemSlots = itemParent.GetComponentsInChildren<ItemSlot>();
        }
    }


    // Use this for initialization
    void Start() {
       

        
        player = GameObject.Find("Player").GetComponent<PlayerManager>();
        
        
    }

    // Update is called once per frame
    void Update()
    {
        RefreshUI();
    }

   public void RefreshUI()
    { 
    
        int i = 0;
        //Debug.Log(player.player.Inventory.items[i].name);
        for (; i < player.player.Inventory.items.Count && i < itemSlots.Length; i++)
        {
            Debug.Log(i);
            itemSlots[i].Item = player.player.Inventory.items[i];
        }

        for (; i < itemSlots.Length; i++)
        {
            itemSlots[i].Item = null;
        }
    }
}
