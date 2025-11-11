# CKB Explorer Web API

[![License](https://img.shields.io/badge/License-MIT-blue.svg)](LICENSE)

CKB Explorer Web API is a REST API service developed based on Spring Boot, providing data query and operation interfaces for the Nervos CKB blockchain explorer.

## Project Overview

CKB Explorer Web API offers comprehensive CKB blockchain data query capabilities, including retrieval and statistical analysis of blocks, transactions, addresses, UDT tokens, and more. This service exposes blockchain data through RESTful interfaces to support frontend applications.

## Technology Stack

- **Backend Framework**: Spring Boot 3.2.10
- **Programming Language**: Java 21
- **Build Tool**: Maven
- **ORM Framework**: MyBatis-Plus
- **API Documentation**: Swagger/OpenAPI 3.0
- **Database**: RisingWave (for data analysis and statistics)
- **Caching**: Redis
- **Service Communication**: Feign

## Environment Requirements

- JDK 21 or higher
- Maven 3.8+ or higher
- Redis 6.0+ (optional, for caching)
- PostgreSQL 14+ database
- RisingWave database

## Project Structure

```
├── src/
│   ├── main/
│   │   ├── java/             # Java source code
│   │   │   └── com/ckb/explorer/    # Main package
│   │   │       ├── controller/      # REST controllers
│   │   │       ├── service/         # Business logic layer
│   │   │       ├── mapper/          # Data access layer
│   │   │       ├── domain/          # Data models
│   │   │       ├── config/          # Configuration classes
│   │   │       └── common/          # Common utilities and constants
│   │   └── resources/        # Configuration files
│   │       ├── application.yml      # Main configuration file
│   │       ├── mapper/              # MyBatis mapping files
│   │       └── static/              # Static resources
│   └── test/                # Test code
└── pom.xml                  # Maven configuration file
```

## Configuration Instructions

### Main Configuration File

The main configuration is located in `src/main/resources/application.yml` and includes:

- Server configuration (port, context path, etc.)
- SpringDoc/Swagger configuration
- MyBatis-Plus configuration
- Database connection configuration

### Environment-Specific Configuration

The project supports multi-environment configuration:

- `application-testnet.yml`: testnet environment configuration
- `application-mainnet.yml`: Mainnet environment configuration

## Security Configuration

This project implements basic access control mechanisms:

1. **Management Interface Protection**: Sensitive operations such as resetting statistical tasks require an access token provided through the `X-Auth-Token` request header
2. **Token Configuration**: Configure `reset.statistics.password` in `application.yml` to set the access token

> Note: Always use a strong randomly generated security token in production environments

## Installation and Running

### Building the Project

```bash
mvn clean install
```

### Running the Application

#### Using Maven

```bash
mvn spring-boot:run
```

#### Using the Startup Script

```bash
chmod +x start.sh
./start.sh
```

## API Documentation

After starting the service, you can access the interactive API documentation at:

```
http://localhost:8081/api/swagger-ui/index.html
```

API endpoint documentation URL:

```
http://localhost:8081/v3/api-docs
```

## Main Function Modules

- **Block Query**: Get block details, latest blocks, block lists, etc.
- **Transaction Query**: Get transaction details, transaction lists, transaction inputs/outputs, etc.
- **Address Query**: Get address information, balances, transaction history, etc.
- **UDT Tokens**: UDT token information query, holder distribution, transaction statistics, etc.
- **DAO Functionality**: DAO depositor query, etc.
- **Statistical Analysis**: Daily statistical tasks, etc.

## Contribution Guidelines

Contributions to this project are welcome! If you want to participate in development, please follow these steps:

1. Fork this repository
2. Create your feature branch (`git checkout -b feature/amazing-feature`)
3. Commit your changes (`git commit -m 'Add some amazing feature'`)
4. Push to the branch (`git push origin feature/amazing-feature`)
5. Open a Pull Request

## License

This project is licensed under the MIT License. See the [LICENSE](LICENSE) file for details.

## Contact

If you have any questions or suggestions, please contact us through:

- Project Repository: [https://github.com/appfi5/ckb-explorer-web.git](https://github.com/appfi5/ckb-explorer-web.git)
- Issues: [https://github.com/appfi5/ckb-explorer-web/issues](https://github.com/appfi5/ckb-explorer-web/issues)