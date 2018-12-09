using System.Collections;
using System.Collections.Generic;
using UnityEngine;

public class ClassDatabase : MonoBehaviour {

    public List<CharacterClass> Classes;
	
    public CharacterClass FindClass(string str)
    {
        for( int i = 0; i < Classes.Count; i++)
        {
            
            if(str == Classes[i].name)
            {
                
                return Classes[i];
            }
        }
        return null;
    }
}
