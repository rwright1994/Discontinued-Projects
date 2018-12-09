using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using UnityEngine.UI;

public class ListGenerator : MonoBehaviour {

    public GameObject prefab;
    public PlayerManager player;


     void Awake()
    {
        player = GameObject.Find("Player").GetComponent<PlayerManager>();
    }

    // Use this for initialization
    void Start () {

       
        GenerateSlots();
       

	}
	
	// Update is called once per frame
	void Update () {
		
	}

    public void GenerateSlots()
    {
        for (int i = 0; i < player.player.Champions.Count; i++)
        {
            GameObject obj = Instantiate(prefab, new Vector3(0, 0, 0), Quaternion.identity) as GameObject;
            obj.transform.SetParent(GameObject.FindGameObjectWithTag("List Content").transform, false);
            obj.GetComponent<ListDisplay>().champ = player.player.Champions[i];
        }
    }
}
