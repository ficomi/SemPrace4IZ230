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
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 *
 * @author admin
 */
public class TextToSpeechClass {

    private final String API_KEY;
    private final String URL;
    private final String NAME;
    private final String PROJECT_PATH;
    private final String[] sentences;
   
    public TextToSpeechClass(String API_KEY, String URL, String NAME) {
        this.API_KEY = API_KEY;
        this.URL = URL;
        this.NAME = NAME;
        PROJECT_PATH=getProjectPath();
        sentences = setSentences();
        
    }

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

            OutputStream out = new FileOutputStream(PROJECT_PATH+"/audio/audio.wav");
            byte[] buffer = new byte[1024];
            int length;
            while ((length = in.read(buffer)) > 0) {
                out.write(buffer, 0, length);
            }

            out.close();
            in.close();
            inputStream.close();
        } catch (IOException e) {
        }

    }
     
     private String getProjectPath() {
        return System.getProperty("user.dir");
    }
     
     private String[] setSentences(){
     String []tempSentences = new String[] {"There is _ on the picture",
                                            "There could be _ on the picture",
                                            "There might be _ on the picture"};
     
     
     
     return tempSentences;
     }

}
