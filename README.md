# [AOE - Library of Open Educational Resources](https://github.com/CSCfi/aoe)

# ⚠️ Project Moved ⚠️

## AOE project has been moved to a new location!

The repository is no longer maintained here.  
Find the latest updates and contributions at:

👉 **[Opetushallitus/aoe](https://github.com/Opetushallitus/aoe)** 👈

## Service Component Links in GitHub (mirrored)
- **aoe-data-analytics**
- [aoe-data-services](https://github.com/CSCfi/aoe-data-services)
- [aoe-semantic-apis](https://github.com/CSCfi/aoe-semantic-apis)
- [aoe-streaming-app](https://github.com/CSCfi/aoe-streaming-app)
- [aoe-web-backend](https://github.com/CSCfi/aoe-web-backend)
- [aoe-web-frontend](https://github.com/CSCfi/aoe-web-frontend)

# AOE Data Analytics

Microservices for querying analytics and statistics from databases.

Logical application components are separated in the modular structure of the project.

## Status Check
Status check endpoint provided by Actuator at
<pre>[api]/actuator/health</pre>

## Compile Project
Compile the project with all its modules at root
<pre>mvn package</pre>
OR repackage
<pre>mvn clean package spring-boot:repackage</pre>

## Run Project
Run the project with all its modules at root
<pre>mvn spring-boot:run</pre>
OR individually
<pre>mvn spring-boot:run -pl [module]</pre>
