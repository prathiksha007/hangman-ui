package com.hangman.hangmanui;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Controller
public class HangmanController {
    int attempts = 1;
    Boolean success = false;

    @GetMapping("/newgame")
    public String getWordDash(Model m){
        success = false;
        attempts = 1;
        String uri = "http://localhost:9999/api/worddash";
        RestTemplate restt = new RestTemplate();
        String result = restt.getForObject(uri, String.class);
        m.addAttribute("worddash",result);
        return "index";
    }
    @GetMapping("/getword")
    public void getWord(Model m){
        String uri = "http://localhost:9999/api/word";
        RestTemplate restt = new RestTemplate();
        String result = restt.getForObject(uri, String.class);
        m.addAttribute("word",result);
    }

    @PostMapping("/new")
    public String valueSubmit(@ModelAttribute EnteredChar enteredChar,  Model model) {
        String w = "";

        if (attempts <= 5 && success == false) {

            model.addAttribute("enteredChar", enteredChar);
            String uri = "http://localhost:9999/api/character";
            RestTemplate restt = new RestTemplate();
            String result_json  = restt.postForObject(uri, enteredChar.getEnteredVal(), String.class);
            JSONObject result = new JSONObject(result_json);
             w = result.getString("word");
            if ((result.getBoolean("valueMatched"))) {
                model.addAttribute("updateddash", result.get("updatedDash"));
                success = result.getBoolean("success");
                if (success){
                     model.addAttribute("word", w);
                     return "success";
                } else {
                    return "updatedindex";
                }

            } else {
                attempts++;
                model.addAttribute("attempts",attempts);
                model.addAttribute("updateddash", result.get("updatedDash"));
                System.out.println("Here"+result.get("updatedDash"));
                return "attemptfail";
            }
        } else {
             model.addAttribute("word", w);
             return "attemptfailfinal";
        }



    }




}
