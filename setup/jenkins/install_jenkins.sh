#!/bin/bash

# Jenkins Installation Script
# This script installs Jenkins on a Debian-based system
set -e

# Define a log function for better visibility
log() {
    echo "[INFO] $1"
}

# Prompt user for confirmation
read -p "Do you want to install Jenkins? (y/n): " confirm

if [[ "$confirm" != "y" && "$confirm" != "Y" ]]; then
    echo "Installation canceled."
    exit 0
fi

log "Downloading Jenkins keyring"
sudo wget -O /usr/share/keyrings/jenkins-keyring.asc https://pkg.jenkins.io/debian-stable/jenkins.io-2023.key

log "Adding Jenkins repository to sources list"
echo "deb [signed-by=/usr/share/keyrings/jenkins-keyring.asc] https://pkg.jenkins.io/debian-stable binary/" | sudo tee /etc/apt/sources.list.d/jenkins.list > /dev/null

log "Updating package list"
sudo apt-get update

log "Installing Jenkins"
sudo apt-get install -y jenkins

log "Starting Jenkins service"
sudo systemctl start jenkins

log "Enabling Jenkins service to start on boot"
sudo systemctl enable jenkins

log "Jenkins installation completed successfully. You can access Jenkins at http://localhost:8080"
