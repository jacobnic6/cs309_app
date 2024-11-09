package com.coms309.nutrifit.service;

public abstract class ServiceHandler
    {
        protected String success = "{\"message\":\"success\"}";
        protected String failure = "{\"message\":\"failure\"}";

        public static boolean isNumeric(String str){
            if(str == null){
                return false;
            }
           for(char c : str.toCharArray()){
               if(!Character.isDigit(c)){
                   return false;
               }
           }
            return true;
        }

    }
