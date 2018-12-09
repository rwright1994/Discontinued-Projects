using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using UnityEngine.EventSystems;
using UnityEngine.SceneManagement;
using UnityEngine.UI;

public class ChararacterCreationHandler : MonoBehaviour, IPointerClickHandler {

    public Text baseStatsList;
    public Text warningText;
    public Text craftingSkillList;

    public FadeOutText fadeOutText;

    public Dropdown classSelector;
    public InputField ChampionName;


    public string selectedClass;

    public Image img;

    private Player player;

    public PlayerManager playerManager;
    public ClassDatabase classDB;

    void Start()
    {
        warningText.enabled = false;
        classDB = GameObject.Find("Class Database Object").GetComponent<ClassDatabase>();
    }


    public void OnPointerClick(PointerEventData eventData)
    {
        if (selectedClass != null && selectedClass != "Select a Class...")
        {
            if (selectedClass == "Pesent")
            {
                player = new Player(true,8);
                player.addChampion(new Champion(ChampionName.text, 52,classDB.FindClass("Peasant").name,classDB.FindClass("Peasant").characterclass.Stats,
                                                classDB.FindClass("Peasant").characterclass.StatsUp, classDB.FindClass("Peasant").characterclass.Resource,
                                                "Sprites/Champions/Pesent/Idle (1)", 1,0));
                playerManager.player = player;
            }
            else if (selectedClass == "Student")
            {
                player = new Player(true, 2);
                player.addChampion(new Champion(ChampionName.text, 36, classDB.FindClass("Student").name, classDB.FindClass("Student").characterclass.Stats, 
                                                classDB.FindClass("Student").characterclass.StatsUp, classDB.FindClass("Student").characterclass.Resource,
                                                "Sprites/Champions/Student/Idle (1)", 1, 0));
               playerManager.player = player;
            }
            else if (selectedClass == "Scrounger")
            {
                player = new Player(true, 2);
               player.addChampion(new Champion(ChampionName.text, 44, classDB.FindClass("Scrounger").name, classDB.FindClass("Scrounger").characterclass.Stats,
                                                classDB.FindClass("Scrounger").characterclass.StatsUp, classDB.FindClass("Scrounger").characterclass.Resource,
                                                "Sprites/Champions/Scrounger/Idle__000", 1, 0));
                playerManager.player = player;
            }
            else
            {
                warningText.enabled = true;
                
                fadeOutText.FadeOut();
               
            }

            if(player != null)
            SceneManager.LoadScene("City - Main");

        }
    }

    public void Dropdown_IndexChanged(int index)
        {
            selectedClass = classSelector.options[index].text;
            if (selectedClass == "Pesent")
            {
                baseStatsList.text = "Strength: 6\nConstitution: 6\nDexterity: 4\n Luck:1\nIntellegence: 2\nWill Power: 3";
                craftingSkillList.text = "Mining: begginer \nFarming: begginer \nForestry: begginer";
                img.sprite = Resources.Load<Sprite>("Sprites/Champions/Pesent/Idle (1)");
              Debug.Log(classDB.FindClass("Peasant").name);

    
            }
            else if (selectedClass == "Student")
            {
                baseStatsList.text = "Strength: 2\nConstitution: 3\nDexterity: 2\n Luck:3\nIntellegence: 6\nWill Power: 6";
                img.sprite = Resources.Load<Sprite>("Sprites/Champions/Student/Idle (1)");
                Debug.Log("Student Selected");
            }
            else if (selectedClass == "Scrounger")
            {
                baseStatsList.text = "Strength: 3\nConstitution: 4\nDexterity: 6\n Luck:4\nIntellegence: 3\nWill Power: 2";
                img.sprite = Resources.Load<Sprite>("Sprites/Champions/Scrounger/Idle__000");
                Debug.Log("Scrounger Selected");
            }
        }
        




    
}
