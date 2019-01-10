/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.semprace.semprace4iz230.UI;

import com.semprace.semprace4iz230.VisualRecognition.VisualRecognitionClass;

/**
 *
 * @author admin
 */
public class UI {

   private final VisualRecognitionClass VRC;
   MainPanel panel;
    
    public UI(VisualRecognitionClass vrc) {
        VRC=vrc;
        startUI();
    }
    
    
    
    
    private void startUI(){
    panel=new MainPanel(VRC);
    panel.setVisible(true);
    }
    
}
