/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.semprace.semprace4iz230.VisualRecognition;

import com.github.sarxos.webcam.Webcam;
import com.ibm.watson.developer_cloud.service.exception.NotFoundException;
import com.ibm.watson.developer_cloud.service.exception.RequestTooLargeException;
import com.ibm.watson.developer_cloud.service.exception.ServiceResponseException;
import com.ibm.watson.developer_cloud.service.security.IamOptions;
import com.ibm.watson.developer_cloud.visual_recognition.v3.VisualRecognition;
import com.ibm.watson.developer_cloud.visual_recognition.v3.model.ClassifiedImages;
import com.ibm.watson.developer_cloud.visual_recognition.v3.model.ClassifyOptions;
import java.awt.Image;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.imageio.ImageIO;
import javax.swing.Icon;
import javax.swing.ImageIcon;

/**
 * Tato t��da se star� o rozpozn�n� co je na obrazku pomoci IBM Watsnu.
 * @author admin
 */
public class VisualRecognitionClass {

    private final String API_KEY; // API kl��.
    private final String URL; // Adresa k zasl�n� dotazu.
   
    private final String PROJECT_PATH; //Cesta k projektu.
    private final Webcam WEBCAM;// objekt webkamera.

    private HashMap<String, Double> visualRecognitionDataForTextToSpeech;

    public VisualRecognitionClass(String apikey, String name, String url) {
        PROJECT_PATH = getProjectPath();
        WEBCAM = getWebcam();
        API_KEY = apikey;
       
        URL = url;
        visualRecognitionDataForTextToSpeech = new HashMap<>();
    }

    public String getAPI_KEY() {
        return API_KEY;
    }


    public String getURL() {
        return URL;
    }

    /**
     * Zas�l� obr�zek ke klasifikaci za pomoc� defaultn�ch klasifik�tor� na servery IBM Watsnu.
     * @param fileName N�zev souboru obr�zku.
     * @param filePath Cesta k obr�zku.
     * @param modelName Id klassifik�toru.
     * @return string Zji�t�n� vlastnosti podle klassifik�toru, 
     * @throws FileNotFoundException Nenalezen soubor obr�zku
     * @throws NotFoundException Nenalezena �adn� data k zad�n�mu klasifik�toru,
     * @throws RequestTooLargeException Obr�zek je moc Velik�.
     * @throws ServiceResponseException Chyba server� IBM.
     */
    
    public String getPictureRecognizedByExistingModel(String fileName, String filePath, VisualRecognitionModelsAvailable modelName) throws FileNotFoundException {
        try {
            IamOptions options = new IamOptions.Builder()
                    .apiKey(API_KEY)
                    .build();
            VisualRecognition service = new VisualRecognition(getCurrentDay(), options);
            InputStream imagesStream = new FileInputStream(filePath);
            ClassifyOptions classifyOptions = new ClassifyOptions.Builder()
                    .imagesFile(imagesStream)
                    .imagesFilename(fileName)
                    .classifierIds(Arrays.asList(modelName.toString().toLowerCase()))
                    .build();
            ClassifiedImages result = service.classify(classifyOptions).execute();

            System.out.println(result.toString());
            return getRecivedInfromationToReadableString(result.toString());
            // Invoke a Visual Recognition method
        } catch (NotFoundException e) {

            return "Nenalezen klasifik�tor: \"" + modelName.toString() + "\" pro tento obr�zek";
            // Handle Not Found (404) exception
        } catch (RequestTooLargeException e) {

            return "Obr�zek je moc velik� ";
            // Handle Request Too Large (413) exception
        } catch (ServiceResponseException e) {

            // Base class for all exceptions caused by error responses from the service
            return "Service returned status code " + e.getStatusCode() + ": " + e.getMessage();
        }

    }
/**
     * Zas�l� obr�zek ke klasifikaci za pomoc� vlastn�ho klasifik�toru na servery IBM Watsnu.
     * @param fileName N�zev souboru obr�zku.
     * @param filePath Cesta k obr�zku.
     * @param threshold Threshold ke klasifikaci.
     * @return string Zji�t�n� vlastnosti podle klassifik�toru, 
     * @throws FileNotFoundException Nenalezen soubor obr�zku
     * @throws NotFoundException Nenalezena �adn� data k zad�n�mu klasifik�toru,
     * @throws RequestTooLargeException Obr�zek je moc Velik�.
     * @throws ServiceResponseException Chyba server� IBM.
     */
    public String getPictureRecognizedByCustomModel(String fileName, String filePath, float threshold) throws FileNotFoundException {
        try {
            IamOptions options = new IamOptions.Builder()
                    .apiKey(API_KEY)
                    .build();
            VisualRecognition service = new VisualRecognition(getCurrentDay(), options);
            InputStream imagesStream = new FileInputStream(filePath);
            ClassifyOptions classifyOptions = new ClassifyOptions.Builder()
                    .imagesFile(imagesStream)
                    .imagesFilename(fileName)
                    .threshold(threshold)
                    .owners(Arrays.asList("me"))
                    .build();
            ClassifiedImages result = service.classify(classifyOptions).execute();

            return getRecivedInfromationToReadableString(result.toString());

        } catch (NotFoundException e) {

            return "Nenalezen klasifik�tor pro tento obr�zek";
            // Handle Not Found (404) exception
        } catch (RequestTooLargeException e) {

            return "Obr�zek je moc velik� ";
            // Handle Request Too Large (413) exception
        } catch (ServiceResponseException e) {

            // Base class for all exceptions caused by error responses from the service
            return "Service returned status code " + e.getStatusCode() + ": " + e.getMessage();
        }
    }
/**
 * Tato funkce se star� o v�ber informac� ze z�skan�ho JSONU, kter� n�m zaslal IBM Watson.
 * @return string V�sledn� data pro zobrazen�.
 */
    private String getCurrentDay() {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        return dateFormat.format(Calendar.getInstance().getTime());
    }

    private String getRecivedInfromationToReadableString(String string) {
        // reg exp (("class":"(.*?)",)|({"class":"w*",))|(("score":\d.\d*},)|("score":\d.\d*})|("score":\d.\d*,))
        String tempString = "";
        string = string.replace("\n", "");
        string = string.replace(" ", "");
        string = string.substring(0, string.length() - 6);
        System.out.println(string);
        Pattern pattern = Pattern.compile("((\"class\":\"(.*?)\",)|(\\{\"class\":\"w*\",))|((\"score\":\\d.\\d*\\},)|(\"score\":\\d.\\d*\\})|(\"score\":\\d.\\d*,))");
        Matcher matcher = pattern.matcher(string);
        while (matcher.find()) {
            tempString += matcher.group();
        }
         System.out.println(tempString);
        tempString = tempString.replace("{", "");
        tempString = tempString.replace("}", "");
        tempString = tempString.replace("\"", "");

        tempString = tempString.replace(":", " ");
        System.out.println(tempString);
//        tempString = tempString.replace(":", ",");
        tempString = tempString.replace("class ", "");
        tempString = tempString.replace("score ", "");
        tempString = tempString.replace(" ", "");
        String[] tempStringTomap = tempString.split(",");
        System.out.println(tempString);
        System.out.println(tempStringTomap.length);
//        for (int i = 0; i < tempStringTomap.length; i++) {
//            System.out.println(tempStringTomap[i]);
//        }
        tempString = "";
        setVisualRecHashMap(tempStringTomap);

        if (tempStringTomap.length <= 2) {
            tempString = tempStringTomap[0] + ": " + tempStringTomap[1] + "<br>";
        } else {

            for (int i = 0; i < tempStringTomap.length - 1; i++) {

                if (i % 2 == 0) {
                    tempString += tempStringTomap[i] + ": " + tempStringTomap[i + 1] + "<br>";
                }
            }

        }

        tempString = "<html>" + tempString + "</html>";
        System.out.println(tempString);
        return tempString;
    }
/**
 * Z�sk� webkameru na tomto po��ta�i.
 * @return Webcam Object webcamery,
 */
    public Webcam getWebcam() {
        Webcam webcam = Webcam.getDefault();
        return webcam;
    }
/**
 * Z�sk�v� obrazek z webkamery.
 * @return String Zprava o z�skan� obrazku.
 * @throws IOException Chyba p�i vytv��en� souboru.
 */
    public String getImageFromCamera() throws IOException {
        WEBCAM.open();
        BufferedImage image = WEBCAM.getImage();
        ImageIO.write(image, "JPG", new File(PROJECT_PATH + "/pictures/camera.jpg"));
        WEBCAM.close();

        return "Obr�zek z kamery je ulozen";
    }

    private String getProjectPath() {
        return System.getProperty("user.dir");
    }

    public String getPROJECT_PATH() {
        return PROJECT_PATH;
    }
/**
 * M�n� velikost obr�zku aby se ve�el do okna programu.
 * @param img Vstupn� obr�zek.
 * @return icon V�stupn� obr�zek.
 */
    public Icon getResizedImage(BufferedImage img) {

        ImageIcon icon;
        if (img.getWidth() > 2000 || img.getHeight() > 2000) {
            icon = new ImageIcon(img.getScaledInstance(img.getWidth() / 8, img.getHeight() / 8, Image.SCALE_DEFAULT));
        } else if (img.getWidth() > 1000 || img.getHeight() > 1000) {
            icon = new ImageIcon(img.getScaledInstance(img.getWidth() / 4, img.getHeight() / 4, Image.SCALE_DEFAULT));
        } else if (img.getWidth() > 600 || img.getHeight() > 600) {
            icon = new ImageIcon(img.getScaledInstance(img.getWidth() / 3, img.getHeight() / 3, Image.SCALE_DEFAULT));
        } else {
            icon = new ImageIcon(img);
        }

        return icon;
    }
/**
 * Ukl�da data do mapy, kter� je pak po�adov�na pro TextToSpeech.
 * @param strings Z�skan� informace.
 */
    public void setVisualRecHashMap(String[] strings) {
        visualRecognitionDataForTextToSpeech.clear();
        try {
            for (int i = 0; i < strings.length - 1; i++) {
            if (i % 2 == 0) {
                visualRecognitionDataForTextToSpeech.put(strings[i], Double.parseDouble(strings[i + 1]));
            }
        }
            
            
        } catch (NumberFormatException e) {
            visualRecognitionDataForTextToSpeech.clear();
        }
        

    }

    public HashMap<String, Double> getVisualRecognitionDataForTextToSpeech() {
        return visualRecognitionDataForTextToSpeech;
    }

}
