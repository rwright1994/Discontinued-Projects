using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using UnityEngine.UI;

public class ListDisplay : MonoBehaviour {

    public Champion champ;

    public Image sprite;
    public Text healthText;
    public Text resourceText;
    public Text expText;
    public Text levelText;


    // Use this for initialization
    void Start () {
        sprite.sprite = Resources.Load<Sprite>(champ.SpriteSlug);
        healthText.text = healthText.text + " " + champ.Health.ToString();
        resourceText.text = champ.Resource.Name + " : " + champ.Resource.CurrResource;
        expText.text = expText.text + " : " + champ.Experience;
        levelText.text += champ.Level;

	}
	
	// Update is called once per frame
	void Update () {
		
	}
}
