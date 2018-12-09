using System.Collections;
using System.Collections.Generic;
using UnityEngine;

[System.Serializable]
public class Class {

    public string Name;
    public List<BaseStat> Stats;
    public List<BaseStat> StatsUp;
    public Resource Resource;

    public Class(string name, List<BaseStat> stats, List<BaseStat> statsUp, Resource resource)
    {
        this.Name = name;
        this.Stats = stats;
        this.StatsUp = statsUp;
        this.Resource = resource;
    }

    public List<BaseStat> GetBaseStats()
    {
        return Stats;
    }

    public List<BaseStat> GetStatsUp()
    {
        return StatsUp;
    }
}
