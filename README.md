# Diploma

This is a bachelor thesis in ITMO university:

_Development of a web application for administering the heterogeneous service infrastructure of Yandex *****_

## Problem
### Problem Introduce 

The problem that this work solves is as follows:
* Four heterogeneous services are in operation, each dealing with different entities.
* The number of objects associated with each entity ranges from 5,000 to 10,000.
* Dynamic and updatable connections exists between objects of different entities, with information about these connections being stored exclusively in one service.
* There is no single entry point for accessing information about entities across different services and their connections.
* Gathering information about the resources expanded for a specific client becomes a non-trivial task, leading to an increase in the occurrence of inaccurate tariff calculations.

### Problem Statement

The objective is to create an administration system that meets the following criteria:
* This system will operate with entities of subordinate services and the relationships between them.
* It should provide the capability to administer entities of subordinate services, including:
  * Retrieving various sets of information about relationships and objects, varying in workload.
  * Visualizing relationships in the form of a graph.
  * Constructing a graph at a specified point in the past.
* Support the imposition of depth limitations on graph construction using traversal policies.
* Ensure asynchronous data updates about relationships and entities of subordinate services occur with configurable periodicity.
* Maintain the order of events that occur in subordinate services.

## Solution
### Architecture

![Arhitecture schema](https://github.com/ST-1580/diploma/blob/images/images/architecture.png)

The architecture is structured as follows: it comprises three core services (modules) — collector, updater, and scheduler.
* The collector exclusively retrieves data from the database and interacts with the user 
* The updater is responsible for retrieving data from subordinate services and delivering it to the database. 
* The scheduler oversees the operation of the updater

### Collector
* Connections are collecting using Breadth-First Search (BFS)
* At each step of BFS, compliance with the traversal policy is verified
* Determining the payload volume for connections and objects

User can choose the type of payload. As default uses _light_ connections + _light_ objects and this graph called _light_. Additionally, users have option to include _heavy_ connections (additional passes through connection tables) or/and _heavy_ objects (additional passes through object tables)

The _light_ object or connection is just id and information about subordinate service

### Updater
The Updater receives a list of events that occurred during a specified time interval from subordinate services and then stores this information in the database. One updater is required for each subordinate service. The JSON structure received by the updater is not strongly documented. However, it is essential to identify fields such as ```eventType``` and ```eventTs```, and knowledge of the object ID or the endpoints of a connection is also necessary

### Scheduler
This module is responsible for executing update tasks, and it can be configured with three parameters:
* _Delay_ - the frequency of sending requests to subordinate services
* _Range_
* _Crossing_

_Range_ and _Crossing_ are used to set boundaries within which events from subordinate services will be considered

### Corrector algorithm
Corrector algorithm is a central component of the entire system under development and the part of updater module. It is responsible for populating the connection table, which enables the construction of _light_ graphs. Errors in the corrector algorithm can lead to the emergence of incorrect states within the system, thereby impeding the effective administration of subordinate services. While the detailed steps may be protected under a Non-Disclosure Agreement (NDA), the global overview can be described in three general steps:
1. **Identify Relevant Events**: Locate all events related to objects that occurred within the specified period and have needed status.
2. **Update Relationships**: Update the connections to reflect the state of the events identified in the first step during the corresponding timeframes.
3. **Fill in Undefined Values**: Populate any undefined or missing data as necessary to ensure completeness and accuracy in the system.

## Demo

The code in this repositroy operates four subordinate services: Alpha, Beta, Gamma and Delta. The entity data of this services is as follows:
* Alpha - ```id```, ```name```
* Beta - ```id```, ```epoch```
* Gamma - ```id```, ```isMaster```
* Delta - ```id```, ```name```

Also there are three conections:
* Alpha -> Beta. The info stored in Alpha service. The data is: ```alphaId```, ```betaId``` and ```hash```
* Gamma -> Alpha. The info stored in Gamma service. The data is: ```gammaId```, ```alphaId``` and ```weight```
* Gamma -> Delta. The info stored in Gamma service. The data is: ```gammaId```, ```deltaId```

Moreover, some of entities can be able/disable, while others can only be deleted:
* Able/disable function: Alpha entity, Gamma entity
* Delete function: Beta entity, Delta entity

### How to start
Just clone the repository, run frontend part via ```npm``` and run backend part with database 

### How to add a new entity/connection
![How to add entity/connection](https://github.com/ST-1580/diploma/blob/images/images/demo_how_to_add.gif)

### How to construct _light_ graph
![How to add entity/connection](https://github.com/ST-1580/diploma/blob/images/images/demo_how_to_construct_light.gif)

### How to construct _heavy_ graph
![How to add entity/connection](https://github.com/ST-1580/diploma/blob/images/images/demo_how_to_construct_heavy.gif)
