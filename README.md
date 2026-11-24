# Farm Equipment Booking System

Java-based Farm Equipment Booking System – BCA PBL Project.

## Project description

This beginner-friendly Core Java console application manages farm equipment,
farmer registration, equipment reservations, rental charges, availability checks,
booking cancellation, and persistent text-file storage.

## Features

- Add and view tractors, harvesters, and general farm machines
- Register and view farmers
- Create and view equipment bookings
- Calculate daily rental charges
- Apply a 10% Harvester discount for rentals of 7 or more days
- Reject overlapping bookings
- Check booking date availability
- Cancel active bookings
- Automatically save and load records from text files

## Java requirements

- JDK 21 or newer
- Terminal or command prompt
- No external libraries or database required

## How to compile

Open a terminal in this project folder and run:

```bash
javac *.java
```

## How to run

```bash
java Main
```

## Menu

The application provides options for:

1. Add farm equipment
2. View farm equipment
3. Register farmers
4. View farmers
5. Book equipment
6. View bookings
7. Cancel bookings
8. Check date availability
9. Save records
10. Save and exit

## Data storage

When records are saved, the application creates a `data` folder containing:

- `equipment.txt`
- `farmers.txt`
- `bookings.txt`

The records are loaded automatically when the application starts.

## Java OOP concepts used

- Classes and objects
- Encapsulation using private fields and public methods
- Inheritance through `FarmMachine`, `Tractor`, and `Harvester`
- Polymorphism through overriding `calculateRentalCharge`
- Abstraction through the abstract `Equipment` class
- Exception handling with `try/catch`
- `ArrayList` collections
- Text-file handling with `FileReader`, `FileWriter`, and `BufferedReader`
