package com.example.week52;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
public class SentenceConsumer {
    public Sentence sentence;

    @RabbitListener(queues = "BadWordQueue")
    public void addBadSentence(String s){
        sentence.badSentences.add(s);
        System.out.println("In badSentences: "+sentence.badSentences);
    }

    @RabbitListener(queues = "GoodWordQueue")
    public void addGoodSentence(String s){
        sentence.goodSentences.add(s);
        System.out.println("In goodSentences: "+sentence.goodSentences);
    }

    @RabbitListener(queues = "GetQueue")
    public Sentence getSentence(){
        return sentence;
    }

    public SentenceConsumer() {
        this.sentence = new Sentence();
    }

}
