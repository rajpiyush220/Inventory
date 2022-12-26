# Getting Started

### Reference Documentation


#### Steps to follow
    * Requ doc
    * Use case
    * Seq diagram
    * ER diagram
    * Tech stack (Spring boot, swagger, mysql, spring data jpa, lombok, MapStruct, docker, spring seluth zipkin, elastic search kibana, flyway and many more to go )
    * Unit test (Junit 5)
    * Authentication and authorization (JWT for time being will have oauth2 or some other api )
    * IDE (Your choice)
    * Code coverage (> 80%)
    * Code quality tool (TBD)
    * Deployment(On local for time being and then move to GCP or some other cloud)
	
### Flyway help
### Flyway migration file naming convention
	* Create script file in src/main/resources/db/migration directory with below naming convention
		* <description>.sql Create_Task_Table.sql
	* Make sure to add parent script first otherwise child will execute first and migration will fail
	* run ./gradlew prefixNewMigrations task to generate prefix for newly created migration file
		
## Google java code format
 * Run below command to check and fix format error
	* To check format issue :- ./gradlew goJF(On linux) gradlew goJF(on windows)
	* To correct format issue :- ./gradlew spotlessApply(On linux) gradlew spotlessApply(on windows)
	
## Steps to execute project
 * Import as gradle project 
 * Update application.yml with db details
 * run as spring boot application (it will automatically create required db and tables)

