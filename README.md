# spring-batch-first-app


# Steps to launch Application
     download or clone spring rest-parent and batch project
     
       Git clone https://github.com/saidarao-0424/spring-batch-first-app.git and 
       Git clone https://github.com/saidarao-0424/rest-parent.git

    mvn clean install 
    
# TODO 
   
    
    1).Implement Junit to perform Unit testing.
    2).Enhance application to use Environment variable and Application Properties class
    3).Define a Datasource based on Environment settings.
    4).Configure Own transaction manager.
    5).Implement multi-step based chunk based processing
    6).add logging aspect to measure number of processed records.
    
# Better modularity
 create a separate project to maintain all core Jpa entities and repositories and re-use it in Batch and rest-controller api.


