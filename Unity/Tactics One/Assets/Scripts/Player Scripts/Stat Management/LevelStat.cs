using System.Collections;
using System.Collections.Generic;
using UnityEngine;

public class LevelStat  {

    public int StrengthUp { get; set; }
    public int ConstitutionUp { get; set; }
    public int DexterityUp { get; set; }
    public int LuckUp { get; set; }
    public int IntellegenceUp { get; set; }
    public int WillPowerUp { get; set; }
    public int HealthPerUp { get; set; }
    public int ResourceUp { get; set; }
   
    [Newtonsoft.Json.JsonConstructor]
    public LevelStat(int strengthUp, int constitutionUp, int dexterityUp, int luckUp, int intellegenceUp, int willPowerUp, int healthPerUp , int resourceUp)
    {
        this.StrengthUp = strengthUp;
        this.ConstitutionUp = constitutionUp;
        this.DexterityUp = dexterityUp;
        this.LuckUp = luckUp;
        this.IntellegenceUp = intellegenceUp;
        this.WillPowerUp = WillPowerUp;
        this.HealthPerUp = healthPerUp;
        this.ResourceUp = resourceUp;
    }
	
}
