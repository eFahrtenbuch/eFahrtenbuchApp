package com.example.efahrtenbuchapp;

import com.example.efahrtenbuchapp.eFahrtenbuch.Auto;
import com.example.efahrtenbuchapp.eFahrtenbuch.User;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class JsonConverterUnitTest {
    @Test
    public void addition_isCorrect() {
        assertEquals(4, 2 + 2);
    }
    @Test
    public void jsonConverterAutoTest(){
        Auto auto = new Auto("kennzeichen", "modell", "passwort", 1);
        JSONObject json = new JSONObject();
        try {
            json.put("kennzeichen", auto.getKennzeichen());
            json.put("modell", auto.getModell());
            json.put("passwort", auto.getPasswort());
            json.put("kmStand", auto.getKmStand());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Auto created = Auto.createFromJSON(json);
        Assert.assertEquals(auto, created);
    }
    @Test
    public void jsonConverterUserTest(){
        User user = new User(88, "benutzername", "name", "vorname__");
        JSONObject json = new JSONObject();
        try {
            json.put("benutzername", user.getBenutzername());
            json.put("name", user.getName());
            json.put("vorname", user.getVorname());
            json.put("id", user.getId());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        User created = User.createFromJson(json);
        System.out.println(created.toString());
        Assert.assertEquals(user.getBenutzername(), created.getBenutzername());
        Assert.assertEquals(user.getName(), created.getName());
        Assert.assertEquals(user.getVorname(), created.getVorname());
        Assert.assertEquals(user.getId(), created.getId());
    }
}