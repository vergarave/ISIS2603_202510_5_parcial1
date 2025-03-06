package uniandes.dse.examen1.exceptions;

public class RepeatedStudentException extends Exception {

    private String login;

    public RepeatedStudentException(String login) {
        this.login = login;
    }

    @Override
    public String getMessage() {
        return "El login " + login + " está repetido";
    }

}
