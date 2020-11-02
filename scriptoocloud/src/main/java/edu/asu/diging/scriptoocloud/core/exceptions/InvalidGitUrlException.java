package edu.asu.diging.scriptoocloud.core.exceptions;

public class InvalidGitUrlException extends Exception {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public InvalidGitUrlException(){
        super();
    }

    public InvalidGitUrlException(Throwable e){
        super(e);
    }

    public InvalidGitUrlException(String message){
        super(message);
    }
}
