# Synchrony 

Synchrony is a Spring Boot REST application that allows users to register, upload, view, and delete images associated with their profiles. 
The application leverages the Imgur API to handle image management and uses an H2 in-memory database to store user information.

## Table of Contents

1. [Getting Started](#getting-started)
2. [Prerequisites](#prerequisites)
3. [Installation](#installation)
4. [Usage](#usage)
5. [Endpoints](#endpoints)
8. [Contributing](#contributing)
9. [License](#license)

## Getting Started

To get a local copy of the project up and running, follow these steps.

### Prerequisites

Ensure you have the following software installed on your local machine:

- Java 8 or later
- Maven
- Git

### Installation

1. Clone the repository:
git clone https://github.com/yourusername/synchrony.git

2. Navigate to the project directory:
cd synchrony

3. Install dependencies and build the project:
mvn clean install

The application should now be running on http://localhost:8080.

## Usage

Use any REST client like Postman or Curl to interact with the API endpoints.

## Endpoints

1. Register a user: `POST /api/users/register`
2. Authenticate a user: `POST /api/users/authenticate`
3. Upload an image: `POST /api/images`
4. View all images: `GET /api/images`
5. Delete an image: `DELETE /api/images/{imageId}`
6. View user profile: `GET /api/users/{userId}`

## Contributing

Pull requests are welcome. For major changes, please open an issue first to discuss what you would like to change.

## License

[MIT](LICENSE)

