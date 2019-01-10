package com.semprace.semprace4iz230;

import com.semprace.semprace4iz230.VisualRecognition.VisualRecognitionClass;
import com.semprace.semprace4iz230.UI.UI;

/**
 * Class containing the "main" method
 * prints Hello world! by default - replace with your code
 *
 */
public class Main 
{
    public static void main( String[] args )
    {
        //remove the line below and replace with your code.
        
        VisualRecognitionClass vrc = new VisualRecognitionClass("0NRaEnh1zhdk0sHHlNJvU0PABH9itE8LZiaj03GkAtq3", //api key
                                                                                         "watson-visual-recogn-visualrecogniti-154679917299", // name
                                                                                         "https://gateway.watsonplatform.net/visual-recognition/api"); //url
        
        UI ui = new UI(vrc);
        
        
        
    }
}
