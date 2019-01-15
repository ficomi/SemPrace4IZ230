/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.semprace.semprace4iz230.TextToSpeech;

import com.ibm.watson.developer_cloud.service.security.IamOptions;
import com.ibm.watson.developer_cloud.text_to_speech.v1.TextToSpeech;
import com.ibm.watson.developer_cloud.text_to_speech.v1.model.SynthesizeOptions;
import com.ibm.watson.developer_cloud.text_to_speech.v1.util.WaveUtils;
import com.semprace.semprace4iz230.VisualRecognition.VisualRecognitionClass;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;

/**
 *  Tato tøída se stará o pøevod textu na mluvenou øeè.
 * @author admin
 */
public class TextToSpeechClass {

    private final String API_KEY; // API klíè.
    private final String URL; // URL pro zaslání dotazu.
    private final VisualRecognitionClass VRC; // Tøída VisualRecognitionClass
    private final String PROJECT_PATH; // Cesta k projektové složce.
    private final String[] sentences; // Vzorové vety.
    private HashMap<String, Double> visualRecognitionDataForTextToSpeech;// Informace kobrázku.
 
    public TextToSpeechClass(String API_KEY, String URL, VisualRecognitionClass vrc) {
        this.API_KEY = API_KEY;
        this.URL = URL;
        VRC = vrc;
        PROJECT_PATH = getProjectPath();
        sentences = setSentences();

    }


    /**
     * Tato funkce pøevádí string do .wav souboru pomocí IBM Watsnu.
     * @param string Vstupní text
     */

    public void getTextToSpeechAudio(String string) {
        IamOptions options = new IamOptions.Builder()
                .apiKey(API_KEY)
                .build();

        TextToSpeech textToSpeech = new TextToSpeech(options);
        textToSpeech.setEndPoint(URL);

        try {
            SynthesizeOptions synthesizeOptions
                    = new SynthesizeOptions.Builder()
                            .text(string)
                            .accept("audio/wav")
                            .voice("en-US_AllisonVoice")
                            .build();

            InputStream inputStream
                    = textToSpeech.synthesize(synthesizeOptions).execute();
            InputStream in = WaveUtils.reWriteWaveHeader(inputStream);
           
            OutputStream out = new FileOutputStream(new File(PROJECT_PATH + "/audio/audio.wav"));
            byte[] buffer = new byte[1024];
            int length;
            while ((length = in.read(buffer)) > 0) {
                out.write(buffer, 0, length);
            }

            out.close();
            in.close();
            inputStream.close();
        } catch (IOException e) {
            System.out.println(e);
        }

    }

    private String getProjectPath() {
        return System.getProperty("user.dir");
    }
    
  /**
     * Pøebírá si data, která byla zjištena z obrázku.
     * 
     */  
    private void setVisualRecognitionDataForTextToSpeech() {
         System.out.println("Mapa Prevzata s VRC");
        visualRecognitionDataForTextToSpeech = VRC.getVisualRecognitionDataForTextToSpeech();
       
    }

    
    
   /**
     * Zkontroluje všechna data o obrázku a podle "jistoty" vybere urèité elementy a pøevede je do vìt.
     *  Vetší "jistota" než 0.9 -&gt; Na obrázku je <br>
     *  Vetší "jistota" než 0.8 -&gt; Na obrázku asi je... <br>
     *  Vetší "jistota" než 0.7 -&gt; Na obrázku asi možná je... <br>
     * @return String Výsledná vìta.
     */
    public String getTextFormVisualRecognition() {
        setVisualRecognitionDataForTextToSpeech();
        String finalSentence = "";
        if (!visualRecognitionDataForTextToSpeech.isEmpty()) {
            for (String key : visualRecognitionDataForTextToSpeech.keySet()) {
                if (visualRecognitionDataForTextToSpeech.get(key) >= 0.90) {
                    finalSentence += sentences[0].replaceAll("_", key) + "\n";
                } else if (visualRecognitionDataForTextToSpeech.get(key) >= 0.80) {
                    finalSentence += sentences[1].replaceAll("_", key) + "\n";
                } else if (visualRecognitionDataForTextToSpeech.get(key) >= 0.70) {
                    finalSentence += sentences[2].replaceAll("_", key) + "\n";
                }
            }
        }
        System.out.println("Finalní zpráva: "+finalSentence);
        return finalSentence;
    }
   /**
    * Zde jsou uložene všechny vzorové vìty k urèování co je na obrazku.
    * @return String[] Všechny mozné vìty
    */
    private String[] setSentences() {
        String[] tempSentences = new String[]{"There is _ on the picture.",
            "There could be _ on the picture.",
            "There might be _ on the picture."};

        return tempSentences;
    }

    public String getAPI_KEY() {
        return API_KEY;
    }

    public String getPROJECT_PATH() {
        return PROJECT_PATH;
    }

    public String getURL() {
        return URL;
    }
    
    

}
