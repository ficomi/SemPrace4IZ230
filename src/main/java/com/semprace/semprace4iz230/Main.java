package com.semprace.semprace4iz230;

import com.semprace.semprace4iz230.TextToSpeech.TextToSpeechClass;
import com.semprace.semprace4iz230.VisualRecognition.VisualRecognitionClass;
import com.semprace.semprace4iz230.UI.UI;

/**
 * Hlavní tøída.
 *
 */
public class Main {

    public static void main(String[] args) {
        VisualRecognitionClass vrc = new VisualRecognitionClass("0NRaEnh1zhdk0sHHlNJvU0PABH9itE8LZiaj03GkAtq3", //api key
                "watson-visual-recogn-visualrecogniti-154679917299", // name
                "https://gateway.watsonplatform.net/visual-recognition/api"); //url
        TextToSpeechClass ttsc = new TextToSpeechClass("EJDATlxE696Lv3etyXyIAi95osGxi8rJ3XrFxb1Uq65k",//api key
                "https://stream.watsonplatform.net/text-to-speech/api" //url
                , vrc);
        UI ui = new UI(vrc, ttsc);

    }
}
