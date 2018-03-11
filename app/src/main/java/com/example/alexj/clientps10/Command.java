package com.example.alexj.clientps10;

/**
 * Created by alexj on 26.02.2018.
 */

public class Command {
    String ParamType;
    String Value;

    Command(String tool , String value){
        ParamType =String.valueOf(tool);
        Value = String.valueOf(value);
    }

    @Override
    public String toString() {
       return ParamType + " " + Value;
    }
}
