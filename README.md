# HostelApp

    Computer Science final paper whose goal is to show the importance of software tests, applied in an 
    application that simulates a hostel reservation system.
    The application was created using React for front-end, SpringBoot for back-end and MySQL for data storage. 
    To perform the tests was used JUnit for unity and integration tests and Selenium for end-to-end tests.

## Requeriments
    - Node 15+;
    - Java 11+;
    - MySQL 5.7+;
    - Lombok;
     
## Links (optional):
    - Node 15 https://nodejs.org/en/download/
    - Java 11 https://www.oracle.com/br/java/technologies/javase/jdk11-archive-downloads.html
    - MySQL 5.7 https://dev.mysql.com/downloads/windows/installer/5.7.html;
    - Lombok https://projectlombok.org/download
    
    - Eclise https://www.eclipse.org/downloads/download.php?file=/technology/epp/downloads/release/2021-09/R/eclipse-jee-2021-09-R-win32-x86_64.zip
    - Visual Studio Code https://code.visualstudio.com/download


## Execution steps

  To run both frontend and backend you need to execute the following steps:
  
### Set Environment Variables
    You need to add the path of the files you downloaded: java, mysql and node

### Backend directory
    You need to open your IDE (in my case eclipse) and import maven project: select folder `backend` of the repository.
    After, in Hostel\backend\src\main\resources\application you set informations about your user mysql admin
    
    spring.datasource.username=`username`
    spring.datasource.password=`password`
    
    Finally, run as Java Application the class HostelSpringBootApplication
    
  
### Frontend directory
    You need to open your IDE (in my case Visual Studio Code) and open folder: select folder `frontend` of the repository.
    After, open powershell terminal and excute `npm install` in order to install all of dependencies and then execute `npm start`.
    
### Run Tests
    You just need to right click on the class / package and run as JUnit Test
 
 ## Some frontend screens
 
 ### Login
 
 ![guest-login](https://user-images.githubusercontent.com/33725123/122142643-46fd0c00-ce26-11eb-8931-066bb3b12eac.png)


 ### Guest profile without reservations
 
 ![guest-without-reservations](https://user-images.githubusercontent.com/33725123/122142743-6eec6f80-ce26-11eb-8ba0-380ba84476ca.png)

 
 ### Guest reservation creation
 
 ![create1](https://user-images.githubusercontent.com/33725123/122142980-daced800-ce26-11eb-93d3-d2f382b55231.png)

 ![create2](https://user-images.githubusercontent.com/33725123/122142984-dd313200-ce26-11eb-92ef-a7799f161630.png)

 ![create3](https://user-images.githubusercontent.com/33725123/122142992-df938c00-ce26-11eb-8920-ed69b510f616.png)


 ### Guest profile with reservation
 
 ![guest-with-reservation](https://user-images.githubusercontent.com/33725123/122142810-8cb9d480-ce26-11eb-8828-9fa5b92f1742.png)
 
 
 ### Admin profile
 
 ![admin](https://user-images.githubusercontent.com/33725123/122143039-fb972d80-ce26-11eb-98fd-14ad20296ed0.png)


