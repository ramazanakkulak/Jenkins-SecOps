#!/bin/bash

# Hadolint Installation Script
# This script installs Hadolint on a Debian-based system

# Exit immediately if a command exits with a non-zero status
set -e

# Define a log function for better visibility
log() {
    echo "[INFO] $1"
}

# Prompt user for confirmation
read -p "Do you want to install Hadolint? (y/n): " confirm

if [[ "$confirm" != "y" ]]; then
    echo "Installation canceled."
    exit 0
fi

log "Downloading Hadolint binary"
wget -O hadolint https://github.com/hadolint/hadolint/releases/download/v2.12.0/hadolint-Linux-x86_64

log "Moving Hadolint binary to /usr/local/bin"
sudo mv hadolint /usr/local/bin/hadolint

log "Setting execute permissions for Hadolint"
sudo chmod +x /usr/local/bin/hadolint

log "Verifying Hadolint installation"
hadolint --version

log "Hadolint installation completed successfully"
