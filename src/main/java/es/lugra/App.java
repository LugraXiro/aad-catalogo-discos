package es.lugra;

import java.nio.file.Files;
import java.nio.file.Path;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
        System.out.println( "Hello World!" );
    }

    public static void write(List<Disco> discos, Path outFile) {
        // Asegurar que la carpeta destino existe

        Path parent = outFile.getParent();
        if (parent != null) Files.createDirectories(parent);


        // Crear el documento DOM en blanco
        DocumentBuilder db = DocumentBuilderFactory.newInstance().newDocumentBuilder();
        Document doc = db.newDocument();


        // Crear la raíz <discos>
        Element root = doc.createElement("discos");
        doc.appendChild(root);

        for (Disco d : discos) {
            Element e = doc.createElement("album");

            String id = d.grupo + d.toString().
            e.setAttribute("id", );
        }
    }
}
