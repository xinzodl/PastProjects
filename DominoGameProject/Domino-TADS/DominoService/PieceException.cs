using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;

namespace DominoService
{
    public class PieceException: SystemException{

    private string messgeError;
    public string MessageError{
        get {
            return messgeError;
        }
    }

    public PieceException() { this.messgeError = null; }
    public PieceException(string message) { this.messgeError = message; }
    public PieceException(string message, System.Exception inner) { this.messgeError = message; }
 
    // Constructor needed for serialization 
    // when exception propagates from a remoting server to the client.
    protected PieceException(System.Runtime.Serialization.SerializationInfo info,
        System.Runtime.Serialization.StreamingContext context) {}
    }
}