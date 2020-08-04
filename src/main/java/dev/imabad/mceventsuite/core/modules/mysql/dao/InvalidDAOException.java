package dev.imabad.mceventsuite.core.modules.mysql.dao;

public class InvalidDAOException extends RuntimeException {

    private Class<? extends DAO> invalid;

    public InvalidDAOException(Class<? extends DAO> invalid){
        this.invalid = invalid;
    }

    @Override
    public String getMessage() {
        return "Attempted to access an invalid DAO: " + invalid.getCanonicalName();
    }
}
