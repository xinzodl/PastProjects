using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;

namespace DominoService
{
    public class PileException : SystemException{
  
    private string messgeError;
    public string MessageError{
        get {
            return messgeError;
        }
    }

    public PileException() { this.messgeError = null; }
    public PileException(string message) { this.messgeError = message; }
    public PileException(string message, System.Exception inner) { this.messgeError = message; }
 
    // Constructor needed for serialization 
    // when exception propagates from a remoting server to the client.
    protected PileException(System.Runtime.Serialization.SerializationInfo info,
        System.Runtime.Serialization.StreamingContext context) {}
    }
}