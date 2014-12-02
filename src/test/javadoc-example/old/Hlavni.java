package kolekce;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class Hlavni {

    /**
     * @param args Prvni je jmeno vstupniho souboru, druhy vystupniho.
     */
    public static void main(String[] args) {

        String fileInputName = args[0];
        String fileOutputName = args[1];
        
        VstupDat vstupDat = VstupDat.getInstance();
        
        List<RozvrhovaAkce> seznamRozAkci = vstupDat.nactiRozvrhoveAkce(fileInputName);
        
        zapisDoSouboru(fileOutputName, seznamRozAkci);
    }

    /**
     * Zapise akce do souboru.
     * 
     * @param celeJmenoSouboru Jmeno souboru.
     * @param seznam Seznam akci.
     */
    public static void zapisDoSouboru(String celeJmenoSouboru, List<RozvrhovaAkce> seznam) {

        try {

            FileWriter fstream = new FileWriter(celeJmenoSouboru);
            BufferedWriter out = new BufferedWriter(fstream);

            int i = 1;
            for (RozvrhovaAkce rozAkce : seznam) {

                out.write(i + ". " + rozAkce.toString() + "\n");
                i++;
            }

            out.close();
            fstream.close();            
        }
        catch (IOException e) {

            e.printStackTrace();
        }
    }
    
}
