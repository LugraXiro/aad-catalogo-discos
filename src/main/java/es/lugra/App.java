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
    public static void main( String[] args ) throws Exception
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

        discos.add(new Disco(
            "Jinx",
            "Crumb",
            2019, 6, 14,
            "Psychedelic Pop",
            new String[]{"Cracking", "Nina", "Ghostride", "Fall Down", "The Letter"}
        ));

        discos.add(new Disco(
            "Is This It",
            "The Strokes",
            2001, 7, 30,
            "Indie Rock",
            new String[]{"Is This It", "The Modern Age", "Soma", "Barely Legal", "Someday"}
        ));

        discos.add(new Disco(
            "Sempiternal",
            "Bring Me The Horizon",
            2013, 4, 1,
            "Metalcore",
            new String[]{"Can You Feel My Heart", "The House of Wolves", "Empire", "Sleepwalking", "Shadow Moses"}
        ));

        discos.add(new Disco(
            "Random Access Memories",
            "Daft Punk",
            2013, 5, 17,
            "Electronic",
            new String[]{"Give Life Back to Music", "Get Lucky", "Instant Crush", "Lose Yourself to Dance", "Touch"}
        ));

        discos.add(new Disco(
            "Modal Soul",
            "Nujabes",
            2005, 11, 11,
            "Hip Hop Instrumental",
            new String[]{"Feather", "Ordinary Joe", "Reflection Eternal", "Luv(sic) Part 3", "Flowers"}
        ));

        discos.add(new Disco(
            "Wildlife",
            "La Dispute",
            2011, 10, 4,
            "Post-Hardcore",
            new String[]{"a Departure", "Harder Harmonies", "St. Paul Missionary Baptist Church Blues", "Safer in the Forest", "King Park"}
        ));

        discos.add(new Disco(
            "The Mindsweep",
            "Enter Shikari",
            2015, 1, 19,
            "Electronic Rock",
            new String[]{"The Appeal & The Mindsweep I", "The One True Colour", "Anaesthetist", "The Last Garrison", "Never Let Go of the Microscope"}
        ));

        // Llamar al método write para crear el XML
        Path outputFile = Path.of("discos.xml");
        write(discos, outputFile);

        System.out.println("Archivo XML creado: " + outputFile.toAbsolutePath());
    }

    public static void write(List<Disco> discos, Path outFile) throws Exception {
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

            // Nodo fecha
            Element fecha = doc.createElement("fecha");
            
                // Nodo año
                Element anho = doc.createElement("anho");
                anho.setTextContent(String.valueOf(d.getAnho()));

                // Nodo mes
                Element mes = doc.createElement("mes");
                mes.setTextContent(String.valueOf(d.getMes()));

                // Nodo dia
                Element dia = doc.createElement("dia");
                dia.setTextContent(String.valueOf(d.getDia()));

            fecha.appendChild(anho);
            fecha.appendChild(mes);
            fecha.appendChild(dia);

            //Nodo genero
            Element genero = doc.createElement("genero");
            genero.setTextContent(d.getGenero());

            //Nodo canciones
            Element canciones = doc.createElement("canciones");

                //Nodo canción
                int numeroPista = 1;
                for (String c : d.getCanciones()) {
                    Element cancion = doc.createElement("cancion");
                    cancion.setTextContent(c);

                    cancion.setAttribute("pista", String.valueOf(numeroPista));
                    numeroPista++;

                    canciones.appendChild(cancion);
                }



            //Insertar cositas
            //Recordemos que tiene que ser en el orden contrario.
            album.appendChild(titulo);
            album.appendChild(grupo);
            album.appendChild(fecha);
            album.appendChild(canciones);


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
        t.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");


        try (OutputStream os = Files.newOutputStream(
            outFile,
            StandardOpenOption.CREATE,
            StandardOpenOption.TRUNCATE_EXISTING,
            StandardOpenOption.WRITE)) {

            t.transform(new DOMSource(doc), new StreamResult(os));
        } catch (Exception e) {
            e.printStackTrace();
        }

        /*
        ===================================
        Imprimir XML en consola con pretty print
        ===================================
        */
        System.out.println("\n=== Contenido del XML generado ===\n");
        try {
            t.transform(new DOMSource(doc), new StreamResult(System.out));
            System.out.println(); // Línea en blanco al final
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
