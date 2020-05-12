/*
 * SatelliteDNA.org
 * 
 * 2017
 */
package org.satellitedna.utils;

import java.util.ArrayList;

/**
 *
 * @author clopez
 */
public class Action {
    
    private final String action;
    private final String acted;
    private final ArrayList<Action> actions;
    
    public Action(String action,String acted)
    {
        this.acted = acted;
        this.action = action;
        this.actions = new ArrayList<>();
    }
    public void addAction(Action action)
    {
        this.actions.add(action);
    }

    public String getAction() {
        return action;
    }

    public String getActed() {
        return acted;
    }

    public ArrayList<Action> getActions() {
        return actions;
    }

    @Override
    public String toString() {
        return "Action{" + "action=" + action + ", acted=" + acted + ", actions=" + actions + '}';
    }
    
    
    
}
