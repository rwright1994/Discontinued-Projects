using System.Collections;
using System.Collections.Generic;
using UnityEngine;

public class MonsterGenerator : MonoBehaviour {

    public List<Enemy> EnemyList;
    public GameObject monsterPrefab;

    public int temp;

	void Start () {
        GenerateMonsters();
	}
	
	// Update is called once per frame
	void Update () {
		
	}


    //Generates placeholder monsters within a random range.
    public void GenerateMonsters()
    {
        for(int i = 0; i < temp; i++)
        {
            
            int posInList = (int)Random.Range(0f, 1.9f); 
            GameObject monster = Instantiate(monsterPrefab, new Vector3(0, 0, 0), Quaternion.identity) as GameObject;
            monster.name = EnemyList[posInList].name;
            monster.transform.SetParent(GameObject.FindGameObjectWithTag("ESlot " + i.ToString()).transform, false);
            monster.GetComponent<SpriteRenderer>().sprite = EnemyList[posInList].sprites[posInList];
            monster.GetComponent<EnemyDisplay>().enemy = new Entity(EnemyList[posInList].name, EnemyList[posInList].health, EnemyList[posInList].maxHealth, EnemyList[posInList].mana,
                                                                    EnemyList[posInList].level, EnemyList[posInList].selected, EnemyList[posInList].sprites);
        }
    }
}
