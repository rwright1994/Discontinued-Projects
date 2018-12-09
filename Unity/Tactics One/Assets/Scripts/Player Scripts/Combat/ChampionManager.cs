using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using UnityEngine.UI;

public class ChampionManager : MonoBehaviour {

    public PlayerManager pm;
    public GameObject ChampionPrefab;
    

	// Use this for initialization
	void Start () {

       pm = GameObject.Find("Player").GetComponent<PlayerManager>();
       GenerateChampion();

        
	}
	
	// Update is called once per frame
	void Update () {
		
	}

    //Renders all champions at the start of the battle.
    public void GenerateChampion()
    {
        int pos = 0;
      foreach(Champion champ in pm.player.Champions)
        {
            if (GameObject.Find("Champion Slot " + pos) == null) 
            {
                GameObject Champion = Instantiate(ChampionPrefab, new Vector3(0, 0, 0), Quaternion.identity) as GameObject;
                Champion.name = pm.player.Champions[pos].Name;
                Champion.transform.SetParent(GameObject.FindGameObjectWithTag("Slot " + pos.ToString()).transform, false);
                Champion.GetComponent<SpriteRenderer>().sprite = Resources.Load<Sprite>("Sprites/Pesent/Idle (1)");
              
                Champion.GetComponent<ChampionController>().champion = pm.player.Champions[pos];

                pos++;
            }
        }
 
    }
}
