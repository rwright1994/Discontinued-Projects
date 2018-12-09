using System.Collections;
using System.Collections.Generic;
using UnityEngine.UI;
using UnityEngine.EventSystems;
using UnityEngine;

public class MenuManager : MonoBehaviour, IPointerEnterHandler, IPointerExitHandler, IPointerClickHandler { 

    public Text text;
    public SceneLoader manager;

    public void OnPointerEnter(PointerEventData eventData)
    {
        text.color = Color.red;
        
    }

    public void OnPointerExit(PointerEventData eventData)
    {
        text.color = Color.white;
    }

    public void OnPointerClick(PointerEventData eventData)
    {


        manager.Load(text.text);
        
    }

    // Use this for initialization
    void Start()
    {

    }

    // Update is called once per frame
    void Update()
    {

    }

  
}
