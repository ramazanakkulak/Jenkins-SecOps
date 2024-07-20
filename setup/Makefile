# Clean target
# This target changes directory to the parent directory and runs Maven's clean command to remove any previous build artifacts.
clean:
	(cd .. && mvn clean)

# Build target
# This target changes directory to the parent directory and runs Maven's clean package command to build the project.
# It skips the tests using the -DskipTests option.
build:
	(cd .. && mvn clean package -DskipTests)

## up: starts all containers in the background without forcing a rebuild
up:
	@echo "Starting Docker images..."
	docker-compose up -d
	@echo "Docker images started!"

## up_build: stops docker-compose (if running), builds all projects, and starts docker-compose
up_build:
	@echo "Stopping docker images (if running...)"
	docker-compose down
	@echo "Building (when required) and starting docker images..."
	docker-compose up --build -d
	@echo "Docker images built and started!"

## down: stops docker-compose
down:
	@echo "Stopping docker compose..."
	docker-compose down
	@echo "Done!"
