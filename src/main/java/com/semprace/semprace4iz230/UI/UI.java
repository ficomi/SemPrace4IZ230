/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.semprace.semprace4iz230.UI;

import com.semprace.semprace4iz230.TextToSpeech.TextToSpeechClass;
import com.semprace.semprace4iz230.VisualRecognition.VisualRecognitionClass;

/**
 * Tato tøída se stará o správu uživatelského rozhraní. 
 * @author admin
 */
public class UI {

   private final VisualRecognitionClass VRC;
   private final TextToSpeechClass TTSC;
   MainPanel panel;
    
    public UI(VisualRecognitionClass vrc,TextToSpeechClass ttsc) {
        VRC=vrc;
        TTSC=ttsc;
        startUI();
    }
    
    
    
    
    private void startUI(){
    panel=new MainPanel(VRC,TTSC);
    panel.setVisible(true);
    }
    
}
