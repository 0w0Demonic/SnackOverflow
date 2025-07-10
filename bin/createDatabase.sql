CREATE TABLE Item (
    id INT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(64) NOT NULL,
    description VARCHAR(256)
) ENGINE=InnoDB;

CREATE TABLE Staff (
    id INT PRIMARY KEY AUTO_INCREMENT,
    firstName VARCHAR(32) NOT NULL,
    lastName VARCHAR(32) NOT NULL,
    email VARCHAR(128),
    phone VARCHAR(32)
) ENGINE=InnoDB;

CREATE TABLE Location (
    id INT PRIMARY KEY AUTO_INCREMENT,
    street VARCHAR(64) NOT NULL,
    city VARCHAR(64) NOT NULL,
    country VARCHAR(32) NOT NULL
) ENGINE=InnoDB;

CREATE TABLE MachineModel (
    id INT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(64) NOT NULL,
    slots INT NOT NULL
) ENGINE=InnoDB;

CREATE TABLE Machine (
    id INT PRIMARY KEY AUTO_INCREMENT,
    modelId INT NOT NULL,
    staffId INT NOT NULL,
    locationId INT NOT NULL,
    balance INT NOT NULL DEFAULT 0,
    isRunning BOOLEAN NOT NULL DEFAULT TRUE,
    FOREIGN KEY (modelId) REFERENCES MachineModel(id),
    FOREIGN KEY (staffId) REFERENCES Staff(id),
    FOREIGN KEY (locationId) REFERENCES Location(id)
) ENGINE=InnoDB;

CREATE TABLE Inventory (
    machineId INT NOT NULL,
    itemId INT NOT NULL,
    amount INT NOT NULL DEFAULT 0,
    price INT NOT NULL,
    slot INT NOT NULL,
    PRIMARY KEY (machineId, slot),
    FOREIGN KEY (machineId) REFERENCES Machine(id),
    FOREIGN KEY (itemId) REFERENCES Item(id)
) ENGINE=InnoDB;

CREATE TABLE Purchase (
    id INT PRIMARY KEY AUTO_INCREMENT,
    machineId INT NOT NULL,
    itemId INT NOT NULL,
    price INT NOT NULL,
    slot INT NOT NULL,
    timestamp DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (machineId) REFERENCES Machine(id),
    FOREIGN KEY (itemId) REFERENCES Item(id)
) ENGINE=InnoDB;
   