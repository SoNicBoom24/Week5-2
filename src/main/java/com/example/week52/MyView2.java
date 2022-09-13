package com.example.week52;

import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.*;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.router.Route;

import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.button.Button;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;



@Route(value = "index2")
public class MyView2 extends HorizontalLayout {
    private TextField addWord, addSentence;
    private TextArea goodSentence, badSentence;
    private Button btnGood, btnBad, btnSentence, showSentence;
    private ComboBox<String> goodSelect, badSelect;

    public MyView2(){
        addWord = new TextField("Add Word");
        btnGood = new Button("Add Good Word");
        btnBad = new Button("Add Bad Word");

        goodSelect = new ComboBox("Good Words");
//        goodSelect.setItems(new ArrayList<String>());

        badSelect = new ComboBox("Bad Words");


        addSentence = new TextField("Add Sentence");
        btnSentence = new Button("Add Sentence");
        goodSentence = new TextArea("Good Sentences");
        badSentence = new TextArea("Bad Sentences");
        showSentence = new Button("Show Sentence");

        VerticalLayout v1 = new VerticalLayout();
        VerticalLayout v2 = new VerticalLayout();

        addWord.setWidth("100%");
        btnGood.setWidth("100%");
        btnBad.setWidth("100%");
        goodSelect.setWidth("100%");
        badSelect.setWidth("100%");
        addSentence.setWidth("100%");
        btnSentence.setWidth("100%");
        goodSentence.setWidth("100%");
        badSentence.setWidth("100%");
        showSentence.setWidth("100%");

        v1.add(addWord, btnGood, btnBad, goodSelect, badSelect);
        v2.add(addSentence, btnSentence, goodSentence, badSentence, showSentence);

        v1.setSizeFull();
        v2.setSizeFull();
        add(v1, v2);
        setSizeFull();

        btnGood.addClickListener(event ->{
            String addGoodWord = addWord.getValue();
            List out = WebClient.create()
                    .post()
                    .uri("http://localhost:8080/addGood")
                    .body(Mono.just(addGoodWord),String.class)
                    .retrieve()
                    .bodyToMono(List.class)
                    .block();
            goodSelect.setItems(out);
            Notification notification = Notification.show("Insert "+out.get(out.size()-1)+" to Good Word");
        });

        btnBad.addClickListener(event ->{
            String addBadWord = addWord.getValue();
            List out = WebClient.create()
                    .post()
                    .uri("http://localhost:8080/addBad")
                    .body(Mono.just(addBadWord),String.class)
                    .retrieve()
                    .bodyToMono(List.class)
                    .block();
            badSelect.setItems(out);
            Notification notification = Notification.show("Insert "+out.get(out.size()-1)+" to Bad Word");
        });

        btnSentence.addClickListener(event ->{
            String addSentenceWord = addSentence.getValue();
            String out = WebClient.create()
                    .post()
                    .uri("http://localhost:8080/proof")
                    .body(Mono.just(addSentenceWord),String.class)
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();
            System.out.println(out);
            Notification notification = Notification.show(out);
        });

        showSentence.addClickListener(event ->{
            String addSentenceWord = addSentence.getValue();
            Sentence out = WebClient.create()
                    .get()
                    .uri("http://localhost:8080/getSentence")
                    .retrieve()
                    .bodyToMono(Sentence.class)
                    .block();
            goodSentence.setValue(String.valueOf(out.goodSentences));
            badSentence.setValue((String.valueOf(out.badSentences)));
        });
    }
}
