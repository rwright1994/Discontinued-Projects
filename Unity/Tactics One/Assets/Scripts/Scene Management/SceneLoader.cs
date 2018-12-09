using System.Collections;
using System.Collections.Generic;
using UnityEngine.UI;
using UnityEngine.SceneManagement;
using UnityEngine;

public class SceneLoader : MonoBehaviour {

   
	// Use this for initialization
	void Start () {
		
	}
	
	// Update is called once per frame
	void Update () {
		
	}

    public void Load(string str)
    {
      if(str == "Start Game")
        {
            SceneManager.LoadScene("CharacterCreation");
        }
        else
        {
            SceneManager.LoadScene(str);
        }
    }
}
