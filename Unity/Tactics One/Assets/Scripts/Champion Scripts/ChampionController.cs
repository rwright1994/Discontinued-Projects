using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using UnityEngine.UI;

public class ChampionController : MonoBehaviour {

    public PlayerManager pm;
    public GameObject championPrefab;
    public Champion champion;

    public Image HealthBar;
    public Image ExperienceBar;
    public Image ResourceBar;


	// Use this for initialization
	void Start () {

        
      //  ChangeResource();
      
	}
	
	// Update is called once per frame
	void Update () {
        IsSelected();
        ChangeHealth();
        AddExperience();
	}

    public void IsSelected()
    {
        if(champion.Selected == true)
        {
            this.GetComponent<SpriteRenderer>().sprite = Resources.Load<Sprite>("Sprites/Champions/Pesent/Selected (1)");
        }
        else
        {
            this.GetComponent<SpriteRenderer>().sprite = Resources.Load<Sprite>("Sprites/Champions/Pesent/Idle (1)");
        }
    }

    public void ChangeHealth()     {
        HealthBar.fillAmount = (float)champion.Health / champion.MaxHealth;
        if(champion.Health <= 0)
        {
            Die();
        }
    }

    public void AddExperience()
    {
           
        if(this.champion.Experience >= this.champion.ExpToNext)
        {

        }        

            
            ExperienceBar.fillAmount = (float)this.champion.Experience / this.champion.ExpToNext;
        
    }

    public void ChangeResource()
    {
        this.champion.Resource.CurrResource += 10;
        ResourceBar.fillAmount = (float)champion.Resource.CurrResource / champion.Resource.MaxResource;
    }

    public void Die()
    {
        if(champion.Health <= 0)
        {
            Destroy(transform.parent.gameObject);
        }
    }
       
   

}
