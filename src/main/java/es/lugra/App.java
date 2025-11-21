package es.lugra;

import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

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
        // Crear lista de discos
        List<Disco> discos = new ArrayList<>();

        // Añadir discos de ejemplo
        discos.add(new Disco(
            "The Dark Side of the Moon",
            "Pink Floyd",
            1973, 3, 1,
            "Rock Progresivo",
            new String[]{"Speak to Me", "Breathe", "On the Run", "Time", "Money"}
        ));

        discos.add(new Disco(
            "Abbey Road",
            "The Beatles",
            1969, 9, 26,
            "Rock",
            new String[]{"Come Together", "Something", "Here Comes the Sun"}
        ));

        discos.add(new Disco(
            "Thriller",
            "Michael Jackson",
            1982, 11, 30,
            "Pop",
            new String[]{"Wanna Be Startin' Somethin'", "Thriller", "Beat It", "Billie Jean"}
        ));

        // Llamar al método write para crear el XML
        Path outputFile = Path.of("discos.xml");
        write(discos, outputFile);

        System.out.println("Archivo XML creado: " + outputFile.toAbsolutePath());
    }

    public static void write(List<Disco> discos, Path outFile) {
        // Asegurar que la carpeta destino existe

        Path parent = outFile.getParent();
        if (parent != null) Files.createDirectories(parent);


        /*
        ===================================
        DocumentBuilder/Document (no son AutoCloseable)
        ===================================
        */
        DocumentBuilder db = DocumentBuilderFactory.newInstance().newDocumentBuilder();
        Document doc = db.newDocument();


        /*
        ===================================
        Crear la raíz <discos>
        ===================================
        */
        Element root = doc.createElement("discos");
        doc.appendChild(root);

        for (Disco d : discos) {
            // Nodo padre album
            Element album = doc.createElement("album");

            String idManual = d.getGrupo()
                   + String.valueOf(d.getAnho())
                   + String.valueOf(d.getMes())
                   + String.valueOf(d.getDia());
            album.setAttribute("id", idManual);


            // Nodo titulo
            Element titulo = doc.createElement("titulo");
            titulo.setTextContent(d.getTitulo());

            // Noto grupo
            Element grupo = doc.createElement("grupo");
            grupo.setTextContent(d.getGrupo());



            //Insertar cositas
            //Recordemos que tiene que ser en el orden contrario.
            album.appendChild(titulo);
            album.appendChild(grupo);


            root.appendChild(album);
        }

        /*
        ===================================
        Guardar el XML en disco usando Transformer
        Transformer (no Autocloseable) + OutputStream (sí AutoCloseable)
        ===================================
        */
        Transformer t = TransformerFactory.newInstance().newTransformer();
        t.setOutputProperty(OutputKeys.INDENT, "yes");
        t.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", 2);


        try (OutputStream os = Files.newOutputStream(
            outFile,
            StandardOpenOption.CREATE,
            StandardOpenOption.TRUNCATE_EXISTING,
            StandardOpenOption.WRITE)) {

            t.transform(new DOMSource(doc), new StreamResult(os));
        } catch (Exception e) {
            e.printStackTrace();
        }



        
    }
}
