## Project Details

|Details  |    |
| --- | --- |
| **Project**  | [Project Spec](https://learnonline.gmit.ie/pluginfile.php/130649/mod_assign/intro/Project2019_Part2.pdf) 
| **Course** | BSc (Hons) in Software Development
| **Module** |  Distributed Systems |
| **Author** | [Faris Nassif](https://github.com/farisNassif) |
| **Lecturer** | Dr John French |

## Project Outline
1. Create a gRPC Password Service which will provide Password hashing and Verification. The service will expose a gRPC API with two
methods:

2. Design a REST API for a User Account web service using OpenAPI and SwaggerHub, the API definition should support multiple operations, following the REST architectural style.

3. Develop a User Account service by implementing the API in Java using the Dropwizard microservice framework. The User Account service should use the password utility methods provided by the gRPC Password Service  developed in Part 1.

## Running the Program
1. In your command line terminal <br> `git clone https://github.com/farisNassif/FourthYear_DistributedSystemsProject`
2. Navigate to the <b> User_Service </b> directory <br> `cd user_service`
3. Run the Password Service (Part 1) <br> `java -jar runnable_jars/PasswordServer.jar`
4. Run the User Account Service (Part 2) along with the .yaml file <br> `java -jar runnable_jars/UserService.jar server userApiConfig.yaml`

### Working your way around the Program
1. After completing the steps above have a look at the [API Definition](https://app.swaggerhub.com/apis/farisNassif/UserAPI/1#/) for the User Account Service.
2. Using [Postman](https://www.getpostman.com/) or any other collaboration platform of your choice execute any of the operations outlined in the API Definition.

### Outcomes
The User Account Service correctly intergrates with the previously developed Password Service and returns the desired functionality. Passwords are correctly Hashed and Verified via the Password Service and this is observeable via executing the relevant operations outlined in the API Definition. Appropriate Status Codes are returned based on the body of the Request. All operations outlined in the Brief are implemented and Status Code Validated.

### Issues with the Project
1. <b> Password Validation </b> is Asynchronous <br>
After hours spent trying to get the Validation working Synchronously with appropriate requests I found myself running into issue after issue and had to spend time on other parts of the Project. In the future it will be definitely something I look at since I feel it was something small I was missing, I just couldn't find what.
<br>
2. <b> Password Validation </b> (Again), while it works it does require <i>two</i> Requests <br>
I believe this is due to the way I have the `boolean Result` setup in the Client. The way I have the Boolean implemented isn't optimal and wouldn't be used in a real world scenario however for testing this project it saved me a lot of time at the cost of throwing this issue (bit of a contradiction). After several unsuccessful attempts to debug it, it remains in the final release. Again this is something I will look at in future since it's a bit of a hinderance that probably has a reasonable fix with some more debugging.

## References 

* https://www.baeldung.com/java-password-hashing
* https://howtodoinjava.com/security/how-to-generate-secure-password-hash-md5-sha-pbkdf2-bcrypt-examples/
* https://crackstation.net/hashing-security.htm
* https://stackabuse.com/dropwizard-develop-restful-web-services-faster/
* https://stackoverflow.com/questions/57759714/dropwizard-how-to-add-a-custom-validation-for-get-put
* https://docs.oracle.com/javaee/7/api/javax/ws/rs/core/Response.Status.html
* https://stackoverflow.com/questions/655891/converting-utf-8-to-iso-8859-1-in-java-how-to-keep-it-as-single-byte
*	https://stackoverflow.com/questions/7048745/what-is-the-difference-between-utf-8-and-iso-8859-1
* https://stackoverflow.com/questions/38153880/dropwizard-custom-validation-annotation-not-working

## Running Part 1

```
¬ Download the gRPC_Password_Service_Server .jar file
¬ Run java -jar gRPC_Password_Service_Server.jar
¬ (Optional) Run java -jar gRPC_Password_Service_Client.jar
```
