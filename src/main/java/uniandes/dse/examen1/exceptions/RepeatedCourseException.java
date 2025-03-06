package uniandes.dse.examen1.exceptions;

public class RepeatedCourseException extends Exception {

    private String codigoCurso;

    public RepeatedCourseException(String codigoCurso) {
        this.codigoCurso = codigoCurso;
    }

    @Override
    public String getMessage() {
        return "El curso " + codigoCurso + " está repetido";
    }

}
