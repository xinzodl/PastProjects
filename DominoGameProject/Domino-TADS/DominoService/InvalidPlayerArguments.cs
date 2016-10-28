using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;

namespace DominoService
{
    public class InvalidPlayerArguments : SystemException{

    private string messgeError;
    public string MessageError{
        get {
            return messgeError;
        }
    }

    public InvalidPlayerArguments() { this.messgeError = null; }
    public InvalidPlayerArguments(string message) { this.messgeError = message; }
    public InvalidPlayerArguments(string message, System.Exception inner) { this.messgeError = message; }
 
    // Constructor needed for serialization 
    // when exception propagates from a remoting server to the client.
    protected InvalidPlayerArguments(System.Runtime.Serialization.SerializationInfo info,
        System.Runtime.Serialization.StreamingContext context) {}
    }
}