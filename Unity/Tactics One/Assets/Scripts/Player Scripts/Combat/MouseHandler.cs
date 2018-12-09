using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using UnityEngine.EventSystems;

public class MouseHandler : MonoBehaviour
{

    public PlayerManager pm;
   // public MonsterManager monsterManager;
    public Entity enemyTarget;
    public Champion ChampTarget;

    // Use this for initialization
    void Start()
    {
        pm = GameObject.Find("Player").GetComponent<PlayerManager>();
     //   monsterManager = GameObject.Find("Monster Manager ").GetComponent<MonsterManager>();
    }

    // Update is called once per frame
    void Update()
    {


        ChampionSelect();
        
        EnemySelect();
        if (pm.player.SelectedChampion != null)
            pm.player.SelectedObj.transform.Translate(Vector3.right * Time.deltaTime);

    }

    public void ChampionSelect()
    {
        if (Input.GetMouseButtonDown(0))
        {
            foreach (Champion champ in pm.player.Champions)
            {

                if (ClickSelect() != null && ClickSelect().tag != "Enemy")
                {
                    if (ClickSelect().GetComponent<ChampionController>().champion == champ && pm.player.SelectedChampion == null)
                    {
                        pm.player.SelectedChampion = champ;
                        pm.player.SelectedObj = ClickSelect();
                        champ.Selected = true;
                        Debug.Log("Selected " + champ.Name + " changed selected to true;");
                    }
                    else if (ClickSelect().GetComponent<ChampionController>().champion == champ && pm.player.SelectedChampion != null)
                    {
                        pm.player.SelectedChampion.Selected = false;
                        Debug.Log(pm.player.SelectedChampion.Name + " changed selected to false");
                        pm.player.SelectedChampion = champ;
                        pm.player.SelectedObj = ClickSelect();
                        Debug.Log(pm.player.SelectedChampion.Name + " is now the Selected Champion");
                        champ.Selected = true;
                    }
                    else if (pm.player.SelectedChampion == null && pm.player.Target != null)
                    {
                        pm.player.Target.Selected = false;

                    }
                }
            }
            if (pm.player.SelectedChampion != null && ClickSelect() == null)
            {
                pm.player.SelectedChampion.Selected = false;
                Debug.Log("Changed " + pm.player.SelectedChampion.Name + " To false");
                pm.player.SelectedChampion = null;
                pm.player.SelectedObj = null;
                Debug.Log("Unselected");
            }
            else if (ClickSelect() == null && pm.player.SelectedChampion == null)
            {
                Debug.Log("Nothing is selected");
            }



        }
    }

    public void EnemySelect()
    {
        if (Input.GetMouseButtonDown(0)) { 

            if(ClickSelect().tag == "Enemy" && pm.player.SelectedChampion == null)
            {
                Debug.Log(ClickSelect().GetComponent<EnemyDisplay>().enemy.Name);
            }

            }
    }

    public GameObject ClickSelect()
    {

        RaycastHit2D hit = Physics2D.Raycast(Camera.main.ScreenToWorldPoint(Input.mousePosition), transform.forward, Mathf.Infinity);

        if (hit)
        {

            return hit.transform.gameObject;
        }
        else
        {
            return null;
        }



    }



}
