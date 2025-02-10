package md.utm.lab2;

import java.awt.*;
import java.io.File;
import java.io.IOException;

public class Playground {
    public static void main(String[] args) throws IOException {



        String url = "src/main/resources/visualisation_fa/index.html";
        File htmlFile = new File(url);
        Desktop.getDesktop().browse(htmlFile.toURI());
    }
}