package eus.blankcard.decklearn.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class StudyController {
    
    @GetMapping("/study/{deckId}")
    private String getStudyView() {
        // Aqui vas a venir muchas veces, si es la primera vez tienes que crear algo que guarde la sesuón actual y cree los hilos pasandole el mazo creado.
        // Uno de los hilos va a empezar a elegir la siguiente carta mientras que el otro va a estar durmiendo esperando a que le avise oq ue haya content en el buffer
        // Una vez encontrada la carta se devuelve una vista con la pregunta.

        return "";
    }

    @GetMapping("/study/{deckId}/{cardId}")
    private String getResponseView() {
        // Returneas la vista con la respuesta de esa carta
        
        return "";
    }

    @PostMapping("/study/{deckId}/{cardId}")
    private String saveResponse() {
        // Notificas al hilo de la respuesta para que empieze a seleccionar la siguente carta
        // Aquí pillas el name del boton que te ha mandado el post. Una vez sabes si ha sido respuesta buena o mala tienes que guardarlo en response.
        // Return la vista con la siguiente carta. Si por alguna razón era la última carta y la ha respondido bien puedes pasar a la vista de stats

        return "";
    }
}
