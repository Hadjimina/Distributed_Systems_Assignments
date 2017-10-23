# Answers


## Question 1:
a)  
The Request-Line is the first line of a request which specifies the method applied to the resource, the Request-URI and the HTTP-version in use. The information is separated by SP characters and the Request-Line ends with the CRFL sequence.

Request-Line = Method SP Request-URI SP HTTP-Version CRLF

b)  
GET  / HTTP/1.1 CRLF  
Host: 192.168.1.1:8080 CRLF

c)

- use HTTP methods explicitly and in a way that's consistent with the protocol definition 

- be stateless



## Question 2:
a)  
On the server side, its the class “ServerSocket”, which is described as follows: A server socket waits for requests to come in over the network. It performs some operation based on that request, and then possibly returns a result to the requester.1

On the client side, its the class “Socket”, which is described as follows: A socket is one end-point of a two-way communication link between two programs running on the network. Socket classes are used to represent the connection between a client program and a server program.2

b)  
InputStream can block, if it doesn’t receive any data but hasn’t reached the end of file. Speaking of a socket implementation, this means, there was no FIN flag received. The read()-method blocks.
OutputStream can block, if the output stream is flushed because then all bytes have to be written out. But if the client socket is not ready to receive these bytes, flush() will block.

## Question 3:
a) false

b) false

c) true

d) false

## Question 4:
a)  
It’s the WSDL document of SunSPOTWerService. One can retrieve it by appending ?wsdl to the URL like this:
http://vslab.inf.ethz.ch:8080/SunSPOTWebServices/SunSPOTWebservice?wsdl

b)  
These definitions can be found in the file stored at http://vslab.inf.ethz.ch:8080/SunSPOTWebServices/SunSPOTWebservice?xsd=1 . 
The definition for getSpot is:
```xml
<xs:complexType name="getSpot">
	<xs:sequence><xs:element name="id" type="xs:string" minOccurs="0"/>
	</xs:sequence>
</xs:complexType>
```

The definition for getSpotResponse is:
```xml
<xs:complexType name="getSpotResponse">
	<xs:sequence><xs:element name="return" type="tns:sunSpot" minOccurs="0"/>
	</xs:sequence>
</xs:complexType>
```

c)  
The transport protocol is defined in the “bindings” section of the WSDL document.
```xml
<soap:binding transport="http://schemas.xmlsoap.org/soap/http" style="document"/>
```

soap:adress would change to:
```xml
<soap:address location="http://vslab.inf.ethz.ch:8080/SunSPOTWebServices/SunSPOTWebservice"/>
```

## Question 5:

a)  
10.0.2.15 is the network interface of any virtual router. Because the different emulator have their own routers and therefore are isolated from each other, they can and do have all the same IP adress,  10.0.2.15.

b)  
To the emulated device loopback interface.


c)  
The development machine can be reached at 127.0.2.2

d)  
One has to set up port redirections in the virtual router. This can be done with the android debug bridge. 


