# Order Processing System - Simulation Challenge
Author: `Henry Chen`

A high-performance Java simulation designed to model a kitchen's order placement and pickup lifecycle. This system utilizes concurrent scheduled executors to manage real-time event spacing and staggered courier arrivals.

## Features
* **Rate-Limited Placement:** Uses a `SingleThreadScheduledExecutor` to ensure orders are placed at a strict, predictable cadence.
* **Parallel Pickup Simulation:** Leverages a `ScheduledThreadPool` to simulate multiple couriers arriving independently at random intervals.
* **Thread Safety:** Implements `CountDownLatch` for precise synchronization, ensuring the final report is generated only after all asynchronous tasks complete.
* **JSON Integration:** Robust parsing and serialization using Jackson Databind.

## Prerequisites
* **Java 17** or higher
* **Maven** (for dependency management)
* **Curl** (for API submission)

## Installation & Setup
1. Clone the repository:
    ```bash
   git clone https://github.com/haochen1654/Order-Processing-System.git
   cd Order-Processing-System

2. Build the project:
    ```bash
    gradle build
## How to run
The `Dockerfile` defines a self-contained Java/Gradle reference environment.
Build and run the program using [Docker](https://docs.docker.com/get-started/get-docker/):
```
$ docker build -t challenge .
$ docker run --rm -it challenge --auth=<token>
```
Feel free to modify the `Dockerfile` as you see fit.

If java `21` or later is installed locally, run the program directly for convenience:
```
$ ./gradlew run --args="--auth=<token>"
```

## Discard criteria

`<your chosen discard criteria and rationale here>`
