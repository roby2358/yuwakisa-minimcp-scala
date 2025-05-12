# -*- mode: ruby -*-
# vi: set ft=ruby :

Vagrant.configure("2") do |config|
  # Use Ubuntu 24.04 (Noble Numbat)
  config.vm.box = "ubuntu/noble64"

  # Network settings
  config.vm.network "private_network", ip: "192.168.56.10"

  # Mount the current directory
  config.vm.synced_folder ".", "/home/vagrant/project",
    create: true,
    type: "virtualbox",
    owner: "vagrant",
    group: "vagrant"

  # Provider-specific settings
  config.vm.provider "virtualbox" do |vb|
    vb.memory = "4096"  # 4GB RAM minimum for Claude Code
    vb.cpus = 2
    vb.name = "noble-claude-code"
  end

  # Provision the VM with required dependencies
  config.vm.provision "shell", inline: <<-SHELL
    apt-get update
    apt-get install -y curl git ripgrep

    # Install Node.js 18+
    curl -fsSL https://deb.nodesource.com/setup_18.x | sudo -E bash -
    apt-get install -y nodejs

    # Verify Node.js installation
    echo "Node.js version: $(node -v)"
    echo "npm version: $(npm -v)"

    # Install Claude Code globally using sudo
    # This approach avoids npm prefix config issues
    sudo npm install -g @anthropic-ai/claude-code

    # Add automatic cd to project directory in .bashrc
    echo 'cd ~/project' >> /home/vagrant/.bashrc

    # Create a welcome message
    echo '
    ==================================================
    Ubuntu 24.04 (Noble Numbat) VM set up successfully

    Claude Code should be installed and ready to use!

    To use Claude Code:
    1. SSH into the VM: vagrant ssh
    2. Start Claude Code: claude
    3. Authenticate and start using Claude Code

    Your local directory is mounted at ~/project in the VM
    ==================================================
    ' > /etc/motd
  SHELL
end