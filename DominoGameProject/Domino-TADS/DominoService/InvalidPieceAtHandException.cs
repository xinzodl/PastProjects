using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;

namespace DominoService
{
    public class InvalidPieceAtHandException : SystemException{

    private string messgeError;
    public string MessageError{
        get {
            return messgeError;
        }
    }

    public InvalidPieceAtHandException() { this.messgeError = null; }
    public InvalidPieceAtHandException(string message) { this.messgeError = message; }
    public InvalidPieceAtHandException(string message, System.Exception inner) { this.messgeError = message; }
 
    // Constructor needed for serialization 
    // when exception propagates from a remoting server to the client.
    protected InvalidPieceAtHandException(System.Runtime.Serialization.SerializationInfo info,
        System.Runtime.Serialization.StreamingContext context) {}

    }
}