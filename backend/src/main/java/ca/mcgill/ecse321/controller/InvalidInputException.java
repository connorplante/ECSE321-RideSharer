package ca.mcgill.ecse321.controller;

@SuppressWarnings("serial")
public class InvalidInputException extends Exception{

    public InvalidInputException(String errorMessage){
        super(errorMessage);
    }
}
