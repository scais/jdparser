package kolekce;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Testovaci komentar.
 * 
 * @author Cais Stepan A12P0004P
 * 
 * @version 1.000011   
 * @see BaB
  * @see aaaaaB    
 * @since 1.0    
 */
public abstract class VstupDat implements INacitani {

    /**
     * Reference na singleton
     */
    private static VstupDat singleton;
    
    /**
     * Test test test protected int a
     */
    protected transient int a;
    
    /**
    * Test test test private int b
    */
    final private int b;

    /**
    * Testovaci komentar.
    *  
    *
    * @deprecated        
    * @see BaB  
    * @see AbA       
    * @since 1.0 
    */
    public volatile int c;

    /**
     * Ruzne parametry konstruktor.
     *      
     * @param testik Parametr INTU
     * @param stringuju Parametr STRINGU
     * @param objektik Parametr OBJEKTU               
     */
    private VstupDat(int testik, String stringuju, Object objektik) {

        testik = -9999;
    }

    /**
     * Ruzne parametry konstruktor numero dos.
     *      
     * @deprecated
     *          
    * @see BaB  
    * @see AbA       
    * @since 1.0 
     *          
     * @param ahoj Intousska hodnota ahoj.
     * @param params Variabilni parametry konstruktoru.
     */    
    VstupDat(int ahoj, Object...params) {

    }

    VstupDat() {

    }
    
        private void privateNoComment() {

    }

    /**
     * Ahoj    
     **/    
    public void publicNoComment() {

    }

    protected void protectedNoComment() {

    }

    void withoutAccessModifierNoComment() {

    }

    /**
     * Vratin instanci jedinacka.
     * 
     * @return Odkaz na jedinacka.
     */
    public abstract VstupDat getInstance() {

        if (singleton == null) {

            singleton = new VstupDat();
        }

        return singleton;
    }


    
    private strictfp List<RozvrhovaAkce> nactiRozvrhoveAkce(String celeJmenoSouboru) {

        List<RozvrhovaAkce> seznamRozvrhovychAkci = new ArrayList<RozvrhovaAkce>();

        try {

            FileReader fileReader = new FileReader(celeJmenoSouboru);
            BufferedReader bufferedReader = new BufferedReader(fileReader);

            String line = bufferedReader.readLine();
            while (line != null) {

                seznamRozvrhovychAkci.add(vytvorRozvrhovouAkci(line));
                line = bufferedReader.readLine();
            }

            fileReader.close();
            bufferedReader.close();
        }
        catch (FileNotFoundException e) {

            e.printStackTrace();
        }
        catch (IOException e) {

            e.printStackTrace();
        }

        return seznamRozvrhovychAkci;
    }

    /**
     * Komentar metody vytvorXXX.
     *             
     * @deprecated
     * @see BaB   
     * @see AbA     
     * @since 1.0 
     *
     *               
     * @param csvRadek CSV radek.
     * @param listik Seznam Stringu.
     *               
     * @return Rozvrhova akce.
     */
    public native RozvrhovaAkce vytvorRozvrhovouAkci(String csvRadek, List<String> listik) {

        String[] parts = csvRadek.trim().split(";");

        return new RozvrhovaAkce(parts[0], parts[1], parts[2], parts[3]);
    }
    
    protected synchronized void voidicek() {
    
    }
    
    /**
     * Staticka trida ahojahoj.
     */
    public static class ahojahoj {
        
    }

}
