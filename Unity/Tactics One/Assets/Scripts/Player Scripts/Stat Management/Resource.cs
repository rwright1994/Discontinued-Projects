using System.Collections;
using System.Collections.Generic;
using UnityEngine;

[System.Serializable]
public class Resource  {

    public string Name;
    public int CurrResource;
    public int MaxResource;

    [Newtonsoft.Json.JsonConstructor]
    public Resource(string name, int currResource, int maxResource)
    {
        this.Name = name;
        this.CurrResource = currResource;
        this.MaxResource = maxResource;
    }

    public void IncreaseResource(int amount)
    {
        this.MaxResource += amount;
    }

    public void DecreaseResource(int amount)
    {
        this.MaxResource -= amount;
    }
}
