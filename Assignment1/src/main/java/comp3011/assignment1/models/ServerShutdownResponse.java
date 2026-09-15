package comp3011.assignment1.models;

/*This record stores the message returned to the client 
when a server shutdown is requested.*/
public record ServerShutdownResponse(String message) {}