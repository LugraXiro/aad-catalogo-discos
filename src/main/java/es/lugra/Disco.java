package es.lugra;

public class Disco {
    String titulo;
    String grupo;
    int anho;
    int mes;
    int dia;
    String genero;
    String[] canciones;

    public Disco(String titulo, String grupo, int anho, int mes, int dia, 
                 String genero, String[] canciones) {
        this.titulo = titulo;
        this.grupo = grupo;
        this.anho = anho;
        this.mes = mes;
        this.dia = dia;
        this.genero = genero;
        this.canciones = canciones;
    }

    // Getters
    public String getTitulo() {
        return titulo;
    }
    
    public String getGrupo() {
        return grupo;
    }
    
    public int getAnho() {
        return anho;
    }
    
    public int getMes() {
        return mes;
    }
    
    public int getDia() {
        return dia;
    }
    
    public String getGenero() {
        return genero;
    }
    
    public String[] getCanciones() {
        return canciones;
    }
}
