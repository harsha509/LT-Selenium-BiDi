# LT-Selenium-BiDi

This project is designed for executing Selenium tests in the LambdaTest cloud platform. 
Follow the instructions below to set up and run the tests.

## Table of Contents

- [Prerequisites](#prerequisites)
- [Clone the Project](#clone-the-project)
- [Running the Tests](#running-the-tests)
- [Configuration for Local Execution](#configuration-for-local-execution)
- [Environment Variables](#environment-variables)

## Prerequisites

- Ensure that [Maven](https://maven.apache.org/install.html) is installed and added to your system's PATH.

## Clone the Project

To clone this project, use the following command:

```bash
git clone https://github.com/harsha509/LT-Selenium-BiDi
```

## Running the Tests

To run the tests, execute the following commands in your terminal:

```bash
# This command installs the project and its dependencies
mvn install

# This command runs the tests for the project
mvn test
```

## Configuration for Test Execution

The tests are configured to run on LambdaTest by default. If you wish to run the tests locally, 
please adjust the configuration settings in your test files accordingly.

Environment Variables to run tests on LambdaTest, 
you need to set your LambdaTest credentials as 
environment variables. [Sign up](https://accounts.lambdatest.com/register)
for a LambdaTest account for a trial and obtain your username and access key.

Set the following environment variables in your terminal:
### macOS/Linux

Open your terminal and run the following commands:

```bash
export LT_USERNAME=<your_username>
export LT_KEY=<your_access_key>
```

### Windows

Open Command Prompt and run the following commands:

```bash
set LT_USERNAME=<your_username>
set LT_KEY=<your_access_key>
```

Replace `<your_username>` and `<your_access_key>` with your actual LambdaTest credentials.