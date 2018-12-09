using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using UnityEngine.UI;

public class ItemSlot : MonoBehaviour {

    public Image Image;

    private Item _item;
    public Item Item { 
        get { return _item; }
        set
        {
            _item = value;

            if (_item == null)
            {
                Image.enabled = false;
            }
            else
            {
                Image.sprite = _item.sprite;
                Image.enabled = true;
            }
            
        }
    }


    private void OnValidate()
    {
        if (Image == null)
        {
            Image = GetComponent<Image>();
        }
    }


}
