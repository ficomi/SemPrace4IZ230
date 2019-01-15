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
 *  Tato t��da se star� o p�evod textu na mluvenou �e�.
 * @author admin
 */
public class TextToSpeechClass {

    private final String API_KEY; // API kl��.
    private final String URL; // URL pro zasl�n� dotazu.
    private final VisualRecognitionClass VRC; // T��da VisualRecognitionClass
    private final String PROJECT_PATH; // Cesta k projektov� slo�ce.
    private final String[] sentences; // Vzorov� vety.
    private HashMap<String, Double> visualRecognitionDataForTextToSpeech;// Informace kobr�zku.
 
    public TextToSpeechClass(String API_KEY, String URL, VisualRecognitionClass vrc) {
        this.API_KEY = API_KEY;
        this.URL = URL;
        VRC = vrc;
        PROJECT_PATH = getProjectPath();
        sentences = setSentences();

    }


    /**
     * Tato funkce p�ev�d� string do .wav souboru pomoc� IBM Watsnu.
     * @param string Vstupn� text
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
     * P�eb�r� si data, kter� byla zji�tena z obr�zku.
     * 
     */  
    private void setVisualRecognitionDataForTextToSpeech() {
         System.out.println("Mapa Prevzata s VRC");
        visualRecognitionDataForTextToSpeech = VRC.getVisualRecognitionDataForTextToSpeech();
       
    }

    
    
   /**
     * Zkontroluje v�echna data o obr�zku a podle "jistoty" vybere ur�it� elementy a p�evede je do v�t.
     *  Vet�� "jistota" ne� 0.9 -&gt; Na obr�zku je <br>
     *  Vet�� "jistota" ne� 0.8 -&gt; Na obr�zku asi je... <br>
     *  Vet�� "jistota" ne� 0.7 -&gt; Na obr�zku asi mo�n� je... <br>
     * @return String V�sledn� v�ta.
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
        System.out.println("Finaln� zpr�va: "+finalSentence);
        return finalSentence;
    }
   /**
    * Zde jsou ulo�ene v�echny vzorov� v�ty k ur�ov�n� co je na obrazku.
    * @return String[] V�echny mozn� v�ty
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
