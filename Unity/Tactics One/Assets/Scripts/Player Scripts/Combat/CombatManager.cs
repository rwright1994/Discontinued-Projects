using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using UnityEngine.SceneManagement;

public class CombatManager : MonoBehaviour {

    private Champion champTarget;
    private GameObject ChampObj;

    private Entity TargetEnemy;
    private GameObject EnemyObj;

    public int MonstersRemaining;

	// Use this for initialization
	void Start () {
        MonstersRemaining = GameObject.Find("Monster Generator").GetComponent<MonsterGenerator>().temp;
	}
	
	// Update is called once per frame
	void Update () {
        ChampSelect();
        EndCombat();
	}

    public void ChampSelect()
    {
        if (Input.GetMouseButtonDown(0))
        {
            Debug.Log(champTarget);
            //Select champion when nothing is selected
            if (ClickSelect() != null)
            {
                if (ClickSelect().tag == "Champion" && champTarget == null)
                {
                    champTarget = ClickSelect().GetComponent<ChampionController>().champion;
                    champTarget.Selected = true;
                } else if (champTarget != null && ClickSelect().tag == "Enemy")
                {
                    TargetEnemy = ClickSelect().GetComponent<EnemyDisplay>().enemy;
                    champTarget.Attack(TargetEnemy);
                    champTarget.Selected = false;
                    champTarget = null;
                } else if (champTarget == null && ClickSelect().tag == "Enemy")
                {
                    TargetEnemy = ClickSelect().GetComponent<EnemyDisplay>().enemy;
                    TargetEnemy.Selected = true;
                }
            }
            else if(ClickSelect() == null)
            {
                if (champTarget != null)
                {
                    champTarget.Selected = false;
                    champTarget = null;
                }
                else if(TargetEnemy != null) 
                {
                    TargetEnemy.Selected = false;
                    TargetEnemy = null;
                }
            }
           
        }
    }

    public void EnemySelect()
    {
        if (Input.GetMouseButtonDown(0))
        {

            if (ClickSelect().tag == "Enemy")
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

    public void EndCombat()
    {

        if (GameObject.FindGameObjectsWithTag("Enemy").Length == 0)
        {
            SceneManager.LoadScene("City - Main");
        }

    }

}
