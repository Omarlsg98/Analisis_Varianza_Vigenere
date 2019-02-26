/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package palabramasrepetida;

import com.maximeroussy.invitrode.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

/**
 *
 * @author Luis Oliveros y Omar Sanchez
 */
public class menorVarianza {

    /**
     * @param args the command line arguments
     */
    static String reporte = "";
    static ArrayList<String> claves_usadas = new ArrayList<>();

    static int numeroClavesProbadas = 0;
    static int keySize = 5;

    static String[] menoresClaves = new String[10];
    static float[] menoresVarianzas = new float[10];

    public static void main(String[] args) {
        // TODO code application logic here 
        WordGenerator generator = new WordGenerator();

        String textoPlano = getTextoPlano();
        textoPlano = textoPlano.toUpperCase();
        textoPlano = quitarPuntosComas(textoPlano);
        //Probar claves
        while (keySize < 17) {
            long startTime = System.currentTimeMillis(); //fetch starting time
            while (false || (System.currentTimeMillis() - startTime) < 1800000) {//esperar 30 minutos
                String nuevaClave = generator.newWord(keySize);
                if (!claves_usadas.contains(nuevaClave)) {
                    claves_usadas.add(nuevaClave);
                    reporte += "Clave usada: " + nuevaClave + "\n";
                    String cifrado = encrypt(textoPlano, nuevaClave);
                    float varianza = getVarianza(getFrecuenciasTexto(cifrado));
                    if (numeroClavesProbadas < 10) {
                        menoresClaves[numeroClavesProbadas] = nuevaClave;
                        menoresVarianzas[numeroClavesProbadas] = varianza;
                    } else if (esMenor(varianza)) {
                        int indiceMayor = getIndiceMayorVarianza();
                        menoresClaves[indiceMayor] = nuevaClave;
                        menoresVarianzas[indiceMayor] = varianza;
                    }
                    numeroClavesProbadas++;
                }
            }
            System.out.println("------------palabras_" + keySize);
            guardarData("palabras_" + keySize, reporte);
            crearReporteMenores();
            reporte = "";
            claves_usadas = new ArrayList<>();
            numeroClavesProbadas = 0;
            keySize++;
        }
    }

    static int[] getFrecuenciasTexto(String texto) {
        int[] conteos = new int[26];
        for (int i = 0; i < texto.length(); i++) {
            char c = texto.charAt(i);
            if (c >= 'A' && c <= 'Z') {
                conteos[c - 'A']++;
            }
        }
        reporte += "Frecuencias letras:\n";
        for (int j = 0; j < 26; j++) {
            reporte += (char) (j + 'A') + ": " + conteos[j] + "\n";
        }
        return conteos;
    }

    static float getVarianza(int[] valores) {
        float varianza = 0;

        //hallar promedio
        float promedio = 0;
        for (int i = 0; i < valores.length; i++) {
            promedio += valores[i];
        }
        promedio /= valores.length;

        reporte += "El promedio de las letras es: " + promedio + "\n";

        //varianza
        for (int i = 0; i < valores.length; i++) {
            varianza += Math.pow(valores[i] - promedio, 2);
        }
        varianza /= (valores.length - 1);
        reporte += "La varianza de las letras es: " + varianza + "\n";
        reporte += "------------------------------------------------------\n\n";
        return varianza;
    }

    static String encrypt(String text, final String key) {
        String res = "";
        text = text.toUpperCase();
        for (int i = 0, j = 0; i < text.length(); i++) {
            char c = text.charAt(i);
            if (c < 'A' || c > 'Z') {
                continue;
            }
            res += (char) ((c + key.charAt(j) - 2 * 'A') % 26 + 'A');
            j = ++j % key.length();
        }
        return res;
    }

    static String decrypt(String text, final String key) {
        String res = "";
        text = text.toUpperCase();
        for (int i = 0, j = 0; i < text.length(); i++) {
            char c = text.charAt(i);
            if (c < 'A' || c > 'Z') {
                continue;
            }
            res += (char) ((c - key.charAt(j) + 26) % 26 + 'A');
            j = ++j % key.length();
        }
        return res;
    }

    static String quitarPuntosComas(String texto) {
        String nuevoTexto = "";
        for (int i = 0; i < texto.length(); i++) {
            char c = texto.charAt(i);
            if (c < 'A' || c > 'Z') {
                continue;
            }
            nuevoTexto += c;
        }
        return nuevoTexto;
    }

    static String getTextoPlano() {
        String texto = "";
        File f = new File("C:\\Users\\omarleonardo\\Documents\\DocPer\\Tareas\\Noveno Semestre\\Seguridad informatica\\Descifrado Vigenere\\Vigenere menor varianza\\menor Vigenere\\texto_ plano.txt");
        BufferedReader entrada = null;
        try {
            entrada = new BufferedReader(new FileReader(f));
            String linea;
            while (entrada.ready()) {
                linea = entrada.readLine();
                texto = linea;
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                entrada.close();
            } catch (IOException e1) {
            }
        }
        return texto;
    }

    public static void guardarData(String nombreArchivo, String reporte) {
        FileWriter flwriter = null;
        try {
            //crea el flujo para escribir en el archivo
            flwriter = new FileWriter("C:\\Users\\omarleonardo\\Documents\\DocPer\\Tareas\\Noveno Semestre\\Seguridad informatica\\Descifrado Vigenere\\Vigenere menor varianza\\menor Vigenere\\reportes\\" + nombreArchivo + ".txt");
            //crea un buffer o flujo intermedio antes de escribir directamente en el archivo
            PrintWriter pw = new PrintWriter(flwriter);;

            String[] lineas = reporte.split("\n");
            //escribe los datos en el archivo
            for (int i = 0; i < lineas.length; i++) {
                pw.println(lineas[i]);
            }

            System.out.println("Archivo creado satisfactoriamente..");

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                // Nuevamente aprovechamos el finally para 
                // asegurarnos que se cierra el fichero.
                if (null != flwriter) {
                    flwriter.close();
                }
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }

    }

    private static boolean esMenor(float varianza) {
        for (int i = 0; i < menoresVarianzas.length; i++) {
            if (menoresVarianzas[i] > varianza) {
                return true;
            }
        }
        return false;
    }

    private static int getIndiceMayorVarianza() {
        float mayor = 0;
        int indice = -1;
        for (int i = 0; i < menoresVarianzas.length; i++) {
            if (menoresVarianzas[i] > mayor) {
                mayor = menoresVarianzas[i];
                indice = i;
            }
        }
        return indice;
    }

    private static void crearReporteMenores() {
        String reporte = "Menores varianzas para longitud de clave de " + keySize + "\n\n";
        for (int i = 0; i < menoresVarianzas.length; i++) {
            reporte += menoresClaves[i] + ": " + menoresVarianzas[i] + "\n";
        }
        reporte += "\nNumero de claves probadas: " + numeroClavesProbadas;
        guardarData("menores varianzas\\palabras_" + keySize, reporte);
    }

}
