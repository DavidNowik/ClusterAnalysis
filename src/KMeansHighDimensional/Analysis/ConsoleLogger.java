package KMeansHighDimensional.Analysis;

import java.io.*;

public class ConsoleLogger {

    /**
     * Leitet die Konsole in eine Textdatei um. Alles, was danach mit System.out.println ausgegeben wird, 
     * landet zusätzlich in der Datei.
     */
    public static void redirectConsoleOutput(String filename) {
        try {
            File outDir = new File(getProjectRoot() + File.separator + "materials" + File.separator + "out");
            if (!outDir.exists()) outDir.mkdirs();

            File file = new File(outDir, filename);
            FileOutputStream fos = new FileOutputStream(file);
            PrintStream ps = new PrintStream(fos);

            // System.out wird auf PrintStream gesetzt, sodass alles in die Datei geschrieben wird
            System.setOut(ps);
            System.setErr(ps); // optional, auch Fehlermeldungen in die Datei schreiben

            System.out.println("Console output will now be written to: " + file.getAbsolutePath());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static String getProjectRoot() {
        return new File("").getAbsolutePath(); // Projekt-Root
    }
}