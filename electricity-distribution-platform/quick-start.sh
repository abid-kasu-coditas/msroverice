#!/bin/bash
# Quick Start Script for Electricity Distribution Management Platform

set -e

echo "=========================================="
echo "Electricity Distribution Platform"
echo "Quick Start Setup"
echo "=========================================="
echo ""

# Color codes
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
NC='\033[0m' # No Color

# Check prerequisites
echo -e "${YELLOW}Checking prerequisites...${NC}"

if ! command -v java &> /dev/null; then
    echo -e "${RED}❌ Java 21 JDK not found${NC}"
    exit 1
fi

if ! command -v mvn &> /dev/null; then
    echo -e "${RED}❌ Maven not found${NC}"
    exit 1
fi

if ! command -v docker &> /dev/null; then
    echo -e "${RED}❌ Docker not found${NC}"
    exit 1
fi

if ! command -v docker-compose &> /dev/null; then
    echo -e "${RED}❌ Docker Compose not found${NC}"
    exit 1
fi

echo -e "${GREEN}✓ All prerequisites met${NC}"
echo ""

# Build step
echo -e "${YELLOW}Building all services...${NC}"
mvn clean package -DskipTests -f pom.xml
echo -e "${GREEN}✓ Build completed${NC}"
echo ""

# Docker build step
echo -e "${YELLOW}Building Docker images...${NC}"
docker-compose build
echo -e "${GREEN}✓ Docker images built${NC}"
echo ""

# Start services
echo -e "${YELLOW}Starting services...${NC}"
docker-compose up -d
echo -e "${GREEN}✓ Services started${NC}"
echo ""

# Wait for services to be ready
echo -e "${YELLOW}Waiting for services to be healthy...${NC}"
sleep 10

# Verify services
echo -e "${YELLOW}Verifying services...${NC}"
docker-compose ps

echo ""
echo -e "${GREEN}=========================================="
echo "Setup Complete!"
echo "=========================================="
echo "API Gateway: http://localhost:8080"
echo "Auth Service Swagger: http://localhost:8081/swagger-ui.html"
echo "Customer Service Swagger: http://localhost:8082/swagger-ui.html"
echo ""
echo "Default Login Credentials:"
echo "  Username: superadmin"
echo "  Password: password"
echo ""
echo "Useful Commands:"
echo "  View logs: docker-compose logs -f <service-name>"
echo "  Stop: docker-compose down"
echo "  Stop & remove volumes: docker-compose down -v"
echo "=========================================${NC}"
