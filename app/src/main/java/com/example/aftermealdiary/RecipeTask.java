package com.example.aftermealdiary;

import android.os.AsyncTask;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class RecipeTask extends AsyncTask<String, Void, String> {

    URL recipeDataURI;
    String KEY = "bfe27fe32dde406c8969";
    BufferedReader bufferedReader;

    String line = "";
    String result = "";

    ArrayList<String> recipeArrayList;


    @Override
    protected String doInBackground(String... strings) {

        try {
            recipeDataURI = new URL("http://openapi.foodsafetykorea.go.kr/api/" + KEY + "/COOKRCP01/json/1/100");
            bufferedReader = new BufferedReader(new InputStreamReader(recipeDataURI.openStream()));

            while ((line = bufferedReader.readLine()) != null) {
                result = result.concat(line);
            }

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        JSONParser jsonParser = new JSONParser();
        recipeArrayList = new ArrayList<>();

        try {
            JSONObject jsonObject = (JSONObject) jsonParser.parse(result);
            JSONArray jsonArray = (JSONArray) jsonObject.get("row");

            for (int i = 0; i < jsonArray.size(); i++) {
                JSONObject recipe = (JSONObject) jsonArray.get(i);

                String foodName = (String) recipe.get("RCP_NM");
                String foodType = (String) recipe.get("RCP_PAT2");
                String ingredients = (String) recipe.get("RCP_PARTS_DTLS");
                String howTo = (String) recipe.get("RCP_WAY2");

                String manual01 = (String) recipe.get("MANUAL01");
                String manual02 = (String) recipe.get("MANUAL02");
                String manual03 = (String) recipe.get("MANUAL03");
                String manual04 = (String) recipe.get("MANUAL04");
                String manual05 = (String) recipe.get("MANUAL05");
                String manual06 = (String) recipe.get("MANUAL06");
                String manual07 = (String) recipe.get("MANUAL07");
                String manual08 = (String) recipe.get("MANUAL08");
                String manual09 = (String) recipe.get("MANUAL09");
                String manual10 = (String) recipe.get("MANUAL10");
                String manual11 = (String) recipe.get("MANUAL11");
                String manual12 = (String) recipe.get("MANUAL12");
                String manual13 = (String) recipe.get("MANUAL13");
                String manual14 = (String) recipe.get("MANUAL14");
                String manual15 = (String) recipe.get("MANUAL15");
                String manual16 = (String) recipe.get("MANUAL16");
                String manual17 = (String) recipe.get("MANUAL17");
                String manual18 = (String) recipe.get("MANUAL18");
                String manual19 = (String) recipe.get("MANUAL19");
                String manual20 = (String) recipe.get("MANUAL20");

                String manual_image01 = (String) recipe.get("MANUAL_IMG01");
                String manual_image02 = (String) recipe.get("MANUAL_IMG02");
                String manual_image03 = (String) recipe.get("MANUAL_IMG03");
                String manual_image04 = (String) recipe.get("MANUAL_IMG04");
                String manual_image05 = (String) recipe.get("MANUAL_IMG05");
                String manual_image06 = (String) recipe.get("MANUAL_IMG06");
                String manual_image07 = (String) recipe.get("MANUAL_IMG07");
                String manual_image08 = (String) recipe.get("MANUAL_IMG08");
                String manual_image09 = (String) recipe.get("MANUAL_IMG09");
                String manual_image10 = (String) recipe.get("MANUAL_IMG10");
                String manual_image11 = (String) recipe.get("MANUAL_IMG11");
                String manual_image12 = (String) recipe.get("MANUAL_IMG12");
                String manual_image13 = (String) recipe.get("MANUAL_IMG13");
                String manual_image14 = (String) recipe.get("MANUAL_IMG14");
                String manual_image15 = (String) recipe.get("MANUAL_IMG15");
                String manual_image16 = (String) recipe.get("MANUAL_IMG16");
                String manual_image17 = (String) recipe.get("MANUAL_IMG17");
                String manual_image18 = (String) recipe.get("MANUAL_IMG18");
                String manual_image19 = (String) recipe.get("MANUAL_IMG19");
                String manual_image20 = (String) recipe.get("MANUAL_IMG20");



            }
        } catch (ParseException ex) {
            ex.printStackTrace();
        }


        return result;
    }

}
