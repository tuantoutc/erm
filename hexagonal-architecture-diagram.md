# Hexagonal Architecture Structure Diagram

```
erm_demo/
├── src/
│   └── main/
│       ├── java/
│       │   └── com/
│       │       └── example/
│       │           └── erm_demo/
│       │               ├── application/                 # Application Layer
│       │               │   ├── port/                    # Ports
│       │               │   │   ├── in/                  # Input Ports (Use Cases)
│       │               │   │   │   └── ServiceNameUseCase.java
│       │               │   │   └── out/                 # Output Ports (SPI)
│       │               │   │       └── ServiceNamePort.java
│       │               │   └── service/                 # Service Implementation
│       │               │       └── ServiceNameService.java
│       │               │
│       │               ├── domain/                      # Domain Layer (Core)
│       │               │   ├── entity/                  # Domain Entities
│       │               │   │   └── EntityName.java
│       │               │   ├── enums/                   # Enumerations
│       │               │   │   └── StatusEnum.java
│       │               │   ├── event/                   # Domain Events
│       │               │   │   └── EntityCreatedEvent.java
│       │               │   └── exception/               # Domain Exceptions
│       │               │       └── DomainException.java
│       │               │
│       │               ├── adapter/                     # Adapters Layer
│       │               │   ├── in/                      # Input Adapters (Driving Adapters)
│       │               │   │   ├── rest/                # REST Controllers
│       │               │   │   │   ├── controller/      # Controller Classes
│       │               │   │   │   │   └── EntityController.java
│       │               │   │   │   └── dto/             # DTO (Data Transfer Objects)
│       │               │   │   │       ├── request/     # Request DTOs
│       │               │   │   │       │   └── EntityRequest.java
│       │               │   │   │       └── response/    # Response DTOs
│       │               │   │   │           └── EntityResponse.java
│       │               │   │   └── messaging/           # Message Queue Consumers
│       │               │   │       └── KafkaConsumer.java
│       │               │   │
│       │               │   └── out/                     # Output Adapters (Driven Adapters)
│       │               │       ├── persistence/         # Database Persistence
│       │               │       │   ├── entity/          # JPA Entities
│       │               │       │   │   └── EntityJpaEntity.java
│       │               │       │   ├── mapper/          # Mappers (Entity to JPA)
│       │               │       │   │   └── EntityMapper.java
│       │               │       │   └── repository/      # Spring Data Repositories
│       │               │       │       └── EntityRepository.java
│       │               │       │
│       │               │       └── external/            # External Services
│       │               │           └── ExternalServiceAdapter.java
│       │               │
│       │               ├── config/                      # Configuration Classes
│       │               │   └── AppConfig.java
│       │               │
│       │               ├── util/                        # Utility Classes
│       │               │   └── CommonUtils.java
│       │               │
│       │               └── ErmDemoApplication.java      # Spring Boot Main Class
│       │
│       └── resources/
│           └── application.yaml
└── pom.xml
```

## Key Components Explanation

### Application Layer
- **Ports**: Interfaces that define the contract between the application and the outside world
  - **Input Ports (API)**: Define the operations that can be performed on the application
  - **Output Ports (SPI)**: Define the operations that the application needs from external systems

- **Services**: Implement the business logic and use cases of the application

### Domain Layer
- Contains business entities, value objects, and domain logic
- Independent of frameworks and external concerns
- Has no dependencies on other layers

### Adapter Layer
- **Input Adapters**: Handle requests from the outside world and convert them to calls to the application
  - REST Controllers, Message Consumers, etc.
  
- **Output Adapters**: Implement the interfaces defined by the output ports
  - Database repositories, external API clients, etc.

## Data Flow
1. External request comes to an Input Adapter (e.g., REST Controller)
2. Input Adapter converts request to domain model and calls appropriate Use Case
3. Use Case (Service) implements business logic using domain entities
4. If external data is needed, Service calls Output Port interfaces
5. Output Adapters (implementing Output Ports) interact with external systems
6. Results flow back through the layers to the original caller

## Benefits
- **Testability**: Core domain logic can be tested without external dependencies
- **Flexibility**: Can swap implementations of adapters without changing the core
- **Maintainability**: Clear separation of concerns makes the codebase easier to understand
- **Evolvability**: Can evolve different parts of the system independently
