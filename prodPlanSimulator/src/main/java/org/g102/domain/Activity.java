package org.g102.domain;

public class Activity {

    private String id, description, durationUnit, costUnit;
    private int duration, cost, EarliestStart, EarliestFinish, LatestStart, LatestFinish;

    public Activity(String id, String description, int duration, String durationUnit, int cost, String costUnit) {
        setId(id);
        setDescription(description);
        setDuration(duration);
        setDurationUnit(durationUnit);
        setCost(cost);
        setCostUnit(costUnit);
    }

    public Activity(String id, String description, int duration, String durationUnit) {
        setId(id);
        setDescription(description);
        setDuration(duration);
        setDurationUnit(durationUnit);
        setCostUnit("");
        setCost(0);
    }

    public String getId() {return id;}
    public void setId(String id) {this.id = id;}
    public boolean validateId(String id) {return id.matches("[A-Z][0-9]+");}

    public String getDescription() {return description;}
    public void setDescription(String description) {this.description = description;}
    public boolean validateDescription(String description) {return description.matches("[A-Za-z0-9 ]+");}

    public String getDurationUnit() {return durationUnit;}
    public void setDurationUnit(String durationUnit) {this.durationUnit = durationUnit;}
    public boolean validateDurationUnit(String durationUnit) {return durationUnit.matches("[A-Za-z]+");}

    public String getCostUnit() {return costUnit;}
    public void setCostUnit(String costUnit) {this.costUnit = costUnit;}
    public boolean validateCostUnit(String costUnit) {return costUnit.matches("[A-Za-z]+");}

    public int getDuration() {return duration;}
    public void setDuration(int duration) {this.duration = duration;}
    public boolean validateDuration(int duration) {return duration > 0;}

    public int getCost() {return cost;}
    public void setCost(int cost) {this.cost = cost;}
    public boolean validateCost(int cost) {return cost > 0;}

    public int getEarliestStart() {return EarliestStart;}
    public void setEarliestStart(int EarliestStart) {this.EarliestStart = EarliestStart;}

    public int getEarliestFinish() {return EarliestFinish;}
    public void setEarliestFinish(int EarliestFinish) {this.EarliestFinish = EarliestFinish;}

    public int getLatestStart() {return LatestStart;}
    public void setLatestStart(int LatestStart) {this.LatestStart = LatestStart;}

    public int getLatestFinish() {return LatestFinish;}
    public void setLatestFinish(int LatestFinish) {this.LatestFinish = LatestFinish;}

    public int getSlack() {return LatestFinish - EarliestFinish;}

    public boolean doDelay(int delay) {
        if (delay <= 0) return false;
        setDuration(getDuration() + delay);
        return true;
    }

    @Override
    public boolean equals(Object o){
        return o instanceof Activity && ((Activity) o).id.equals(id);
    }

    @Override
    public int hashCode(){
        return id.hashCode();
    }

    @Override
    public String toString(){
        return id + "( " + duration + " " + durationUnit + " )";
    }

    @Override
    public Activity clone(){
        return new Activity(id, description, duration, durationUnit, cost, costUnit);
    }

}