#!/bin/bash

# Script to run tests for Clinic Management System
# Usage: ./run-tests.sh [unit|integration|all]

set -e

# Colors for output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
NC='\033[0m' # No Color

# Function to print colored output
print_status() {
    echo -e "${GREEN}[INFO]${NC} $1"
}

print_warning() {
    echo -e "${YELLOW}[WARNING]${NC} $1"
}

print_error() {
    echo -e "${RED}[ERROR]${NC} $1"
}

# Default test type
TEST_TYPE="${1:-all}"

print_status "Starting Clinic Management System tests..."
print_status "Test type: $TEST_TYPE"

# Check if Maven is available
if ! command -v mvn &> /dev/null; then
    print_error "Maven is not installed or not in PATH"
    exit 1
fi

# Check if Docker is available for integration tests
if [[ "$TEST_TYPE" == "integration" || "$TEST_TYPE" == "all" ]]; then
    if ! command -v docker &> /dev/null; then
        print_warning "Docker is not available. Skipping integration tests."
        TEST_TYPE="unit"
    fi
fi

# Run unit tests
if [[ "$TEST_TYPE" == "unit" || "$TEST_TYPE" == "all" ]]; then
    print_status "Running unit tests..."
    mvn test -Dtest="**/*Test" -Dspring.profiles.active=test

    if [ $? -eq 0 ]; then
        print_status "Unit tests completed successfully"
    else
        print_error "Unit tests failed"
        exit 1
    fi
fi

# Run integration tests with Testcontainers
if [[ "$TEST_TYPE" == "integration" || "$TEST_TYPE" == "all" ]]; then
    print_status "Running integration tests with Testcontainers..."

    # Start Docker containers for testing
    if command -v docker &> /dev/null; then
        print_status "Starting test databases..."
        docker-compose -f docker-compose.test.yml up -d

        # Wait for databases to be ready
        print_status "Waiting for test databases to be ready..."
        sleep 30

        # Run integration tests
        mvn test -Dtest="**/*IntegrationTest" -Dspring.profiles.active=test

        if [ $? -eq 0 ]; then
            print_status "Integration tests completed successfully"
        else
            print_error "Integration tests failed"
        fi

        # Clean up test containers
        print_status "Cleaning up test containers..."
        docker-compose -f docker-compose.test.yml down -v
    fi
fi

# Generate test report
if [[ "$TEST_TYPE" == "all" ]]; then
    print_status "Generating test report..."
    mvn surefire-report:report

    if [ $? -eq 0 ]; then
        print_status "Test report generated successfully"
        print_status "Report available at: target/site/surefire-report.html"
    fi
fi

print_status "All tests completed!"
print_status "Test results:"
mvn test -q -Dspring.profiles.active=test 2>/dev/null | grep -E "(Tests run|Failures|Errors|Skipped)"

exit 0