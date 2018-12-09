using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using UnityEngine.UI;

public class EnemyDisplay : MonoBehaviour {

    public PlayerManager pm;
    

    public Entity enemy;


    public Image img;
    public Image HealthBar;

   

    void Start()
    {

       pm = GameObject.Find("Player").GetComponent<PlayerManager>();
       
        
        if (enemy.Selected == true)
        {
            enemy.Selected = false;

        }

       
    }

     void Update()
    {

        this.IsSelected();
        this.ChangeHealth();
        
    }

    //Change from unselected sprite to selected sprite
    public void IsSelected()
    {
        if (enemy.Selected == true)
        {

            this.GetComponent<SpriteRenderer>().sprite = enemy.Sprite[1];
        }
        else if (enemy.Selected == false)
        {
            this.GetComponent<SpriteRenderer>().sprite = enemy.Sprite[0];
        }
    }

    //Updates monsters HP bar.
    public void ChangeHealth()
    {
       
        HealthBar.fillAmount = (float)enemy.Health / enemy.MaxHealth;
        if (enemy.Health <= 0)
            Die();
    }


    
    public void Die()
    {
        foreach(Champion champ in pm.player.Champions){
            champ.Experience += 35;
            if (champ.Experience >= champ.ExpToNext)
            {
                champ.LevelUp();
            }
            Debug.Log(champ.Name + " " + champ.ExpToNext);
        }

        Destroy(transform.parent.gameObject);
        
    }  
}
