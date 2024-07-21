#!/bin/bash

# Trivy Installation Script
# This script installs Trivy on a Debian-based system

# Exit immediately if a command exits with a non-zero status
set -e

# Define a log function for better visibility
log() {
    echo "[INFO] $1"
}

# Prompt user for confirmation
read -p "Do you want to install Trivy? (y/n): " confirm

if [[ "$confirm" != "y" ]]; then
    echo "Installation canceled."
    exit 0
fi

log "Updating package lists"
sudo apt-get update -y

log "Installing necessary dependencies"
sudo apt-get install -y wget apt-transport-https gnupg lsb-release

log "Adding Trivy's GPG key"
wget -qO - https://aquasecurity.github.io/trivy-repo/deb/public.key | sudo apt-key add -

log "Adding Trivy's repository"
echo "deb https://aquasecurity.github.io/trivy-repo/deb $(lsb_release -sc) main" | sudo tee /etc/apt/sources.list.d/trivy.list

log "Updating package lists after adding Trivy repository"
sudo apt-get update -y

log "Installing Trivy"
sudo apt-get install -y trivy

log "Trivy installation completed successfully"
