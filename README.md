# This is testing task for DataOx.
## The main goal of this task was a creation of application, which will scrap specific data from the [web-site](jobs.techstars.com)
## Tech stack used for this task is:
- SpringBoot
- Java 17
- SwaggerUI
- com.auth0.java.jwt (for jwt creation)
- OkHttp as the web client
- Jsoup for page parsing
- H2 as the database
- Spring JPA for the ORM
- Docker for containerization
### The application gives user ability to search for any job on this website, using almost all sorting paramethers in relatively friendly UI through SwaggerUI.
### Details about launch instructions are in INSTALL.md

### The interaction with the application is preferred through SwaggerUI which is located on `/ui` endpoint.  
### You can view database state through H2 console which is located on `/db` endpoint. Default username is `1` , default password is `1` 
### If you want to create csv dump for the database(my own dump is already on git in `data` folder), you should:
1. Open h2 console
2. Login into h2 console
3. In console for command writing enter `CALL CSVWRITE('data/JobTable.csv', 'SELECT * from JOB_TABLE');`
4. Navigate to project folder and it\`s `target` subfolder
5. In target folder will be folder `data` with your csv dump file.
6. If you run application from IDE, than `data` folder will be just in your project folder
