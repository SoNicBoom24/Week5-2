package com.example.week52;

import com.vaadin.flow.router.Route;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

@RestController
public class WordPublisher {
    @Autowired
    private RabbitTemplate rabbitTemplate;

    public Word word;

    @RequestMapping(value = "/addBad", method = RequestMethod.POST)
    public ArrayList<String> addBadWord(@RequestBody String s){
        word.badWords.add(s);
        return word.badWords;
    }
    @RequestMapping(value = "/delBad/{word}", method = RequestMethod.GET)
    public ArrayList<String> deleteBadWord(@PathVariable("word") String s){
        word.badWords.remove(s);
        return word.badWords;
    }
    @RequestMapping(value = "/addGood", method = RequestMethod.POST)
    public ArrayList<String> addGoodWord(@RequestBody String s){
        word.goodWords.add(s);
        return word.goodWords;
    }
    @RequestMapping(value = "/delGood/{word}", method = RequestMethod.GET)
    public ArrayList<String> deleteGoodWord(@PathVariable("word") String s){
        word.goodWords.remove(s);
        return word.goodWords;
    }
    @RequestMapping(value = "/proof", method = RequestMethod.POST)
    public String proofSentence(@RequestBody String s){
        boolean good = false;
        boolean bad = false;
        String[] text = s.split(" ");
        for (String t: text) {
            good = word.goodWords.contains(t) || good;
            bad = word.badWords.contains(t) || bad;
        }
        if (good && bad){
            rabbitTemplate.convertAndSend("Fanout", "", s);
            return "Found Good And Bad Word";
        } else if (good) {
            rabbitTemplate.convertAndSend("Direct", "good", s);
            return "Found Good Word";
        } else if (bad) {
            rabbitTemplate.convertAndSend("Direct", "bad", s);
            return "Found Bad Word";
        }
        return "";
    }

    @RequestMapping(value = "/getSentence", method = RequestMethod.GET)
    public Sentence getSentence(){
        return (Sentence) rabbitTemplate.convertSendAndReceive("Direct", "getqueue", "");
    }

    public WordPublisher() {
        this.word = new Word();
        this.word.goodWords.add("happy");
        this.word.goodWords.add("enjoy");
        this.word.goodWords.add("like");
        this.word.badWords.add("fuck");
        this.word.badWords.add("olo");
        this.word.badWords.add("boss");
    }
}
