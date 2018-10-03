package ca.mcgill.ecse321.controller;

public class InvalidInputException extends Exception{

    public InvalidInputException(String errorMessage){
        super(errorMessage);
    }
}
