## Shorest path implementation given an XSD and XML file 

### How to run:

- Clone the project
- Import Maven dependencies 
- Run from IDE or terminal (mvn spring-boot:run)

### Endpoints:

#### The port is 8080 by default

- Get - `/shortestDistanceWithPath`

  Query parameters: `String startNode, String finishNode, String fileName`
  
  fileName – needs to start with “/” so it points to file in resources.

- Get - `/shortestDistance`
 
  Query parameters: `String startNode, String finishNode`

  If this endpoint is used, the fileName will default to “/data.xml”. 

### Response:

The response will be in JSON format and will consist of:

- nodesWithClassification – A LinkedHashMap that will store the node and its classification.

- totalLengthInNodes – Integer that represents the number of nodes required to create a path from startNode
to finishNode
