package com.example.a96llegend.ar4ece.FSM;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import android.util.Log;

public class FSMModel {

    //Store all state's name
    private static List<String> allState;
    //Store all input and their current value, by default it should be false
    private Map<String, Boolean> allInput;
    //Store all boolean equation, first term is the current state, second term is the condition(s),
    //and the last one is thr final state
    private static List<ArrayList<String>> allBooleanEquation;
    private static String currentState;

    public FSMModel(List<String> allEquations){
        //Parsing FSM data
        allBooleanEquation = FSMParser.getBooleanEquation(allEquations);
        allState = FSMParser.getAllStateFromAllBooleanEquation(allBooleanEquation);

        allInput = new HashMap<String, Boolean>();
        List<String> allInputList = FSMParser.getAllInputFromAllBooleanEquation(allBooleanEquation);
        for(int i = 0; i < allInputList.size(); i++){
            allInput.put(allInputList.get(i), false);
        }

        //Set initial state
        currentState = allState.get(0);
    }

    //Reset FSM, which will change all the input value to false
    public void reset(){
        Set<String> inputSet = allInput.keySet();
        currentState = allState.get(0);
        Iterator<String> it = inputSet.iterator();
        while(it.hasNext()){
            allInput.put(it.next(), false);
        }
    }

    //Use for set up button in the View
    public List<String> getAllInputName(){
        List<String> allInputName = new ArrayList<String>();
        Set<String> inputSet = allInput.keySet();
        Iterator<String> it = inputSet.iterator();
        while(it.hasNext()){
            allInputName.add(it.next());
        }
        return allInputName;
    }

    //Use for set up button in the View
    public boolean getInputValueByName(String name){
        return allInput.get(name);
    }

    //Use for set up button in the View
    public List<String> getAllStateName(){
        return allState;
    }

    //Trigger input change and return the new value
    public boolean triggerInputChangeAndGetNewValue(String inputName){
        if(allInput.get(inputName)){
            allInput.put(inputName, false);
        } else {
            allInput.put(inputName, true);
        }
        return allInput.get(inputName);
    }

    //Trigger state change and return the condition(s) cause it
    //When return "", that means no condition is match, stay at same state
    public String triggerStateChange(){
        String conditions = "";
        //Find list of boolean equation start with the current state
        for(int i = 0; i < allBooleanEquation.size(); i++){
            if(allBooleanEquation.get(i).get(0).equals(currentState)){
                if (matchCondition(allBooleanEquation.get(i).get(1))){
                    conditions = allBooleanEquation.get(i).get(1);
                    currentState = allBooleanEquation.get(i).get(2);
                    break;
                }
            }
        }
        return conditions;
    }

    public String getCurrentState(){
        return currentState;
    }

    public int getCurrentStateAsIndex(){
        for (int i = 0; i < allState.size(); i++){
            if(allState.get(i).equals(currentState)){
                return i;
            }
        }
        return -1;
    }


    //Check if the condition(s) is match
    private boolean matchCondition(String allConditions){
        //If the condition is "-", means state will change no matter what input is
        if (allConditions.equals("-")) {
            return true;
        }

        //Check each conditions
        List<String> conditionList = FSMParser.splitConditions(allConditions);
        for(int i = 0; i < conditionList.size(); i++){
            if (conditionList.get(i).charAt(0) == '!'){
                if(allInput.get(conditionList.get(i).substring(1, conditionList.get(i).length()))){
                    return false;
                }
            } else {
                if(!allInput.get(conditionList.get(i))){
                    return false;
                }
            }
        }
        return true;
    }





}
