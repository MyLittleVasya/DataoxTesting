# In this file will be instructions for launch of the application
## Requirements:
### To run this application your computer should have installed either JRE(Java Runtime Executable from Java 17) or Docker
## To run it from command line(Windows) through JRE you should:
1. Fetch this repository to any comfortable place on your PC
2. Open command line and navigate to project folder
3. In project folder navigate to target folder ```cd target```
4. When you there, just write ```java -jar DataOxTesting-0.0.1-SNAPSHOT.jar``` *<--- Name of the -jar can be changed, be careful*
5. After that you will see logs of application launching. If something wrong you\`ll see that either.
### To run it from command line(Windows) through Docker you should:
*If my already created image cannot be accessed*
1. Fetch this repository to any comfortable place on your PC
2. Open command line and navigate to project folder
3. Build image of this project using ```docker build -t<ANY_NAME_YOU_WANT> -f Dockerfile.txt .``` *<--- dot is part of the command*
4. If build was successful(it should :) ) you`ll see on docker desktop newly created image
5. After that return to command line and run it using ```docker run -p 8080:8080 <NAME_OF_YOUR_IMAGE>```
6. If everything is ok, you\`ll see logs of application launching. If something wrong you\`ll see that either.

## Additional configuration
Application is already ready to use. All the configurations are done, however, it can be changed using env variables before launch.
Configurations that can be changed you can see in resources/application.yml. Mostly its endpoints, url to scrap, database credentials.
## SwaggerUI
Application has Swagger UI for comfortable using and debugging. It allows to use main endpoint to scrap data without any additional settings and body creation.  
As said above, the body creation for the request isn\`t needed. The application contains placeholder of body, which as for the 21.01.2024 will display you at least 2 jobs.  
The body can be easily changed, to have less parameters, so that it will find more different jobs.  
The body shouldn\`t contain ```null``` on the paramethers, instead it should be either `[]` or be missing, 
