# General-Practice-Data-Collector/GP Data Collector
The  GP Data Collector is a Java-based mini-project aimed at retrieving healthcare facility (General Practice) information in Queensland, Australia. The project uses the Geoapify API to fetch clinic details and stores the data in an Excel file for easy access and analysis.

# Features
.Fetches General Practice clinic data in Queensland, Australia.
. Uses Geoapify API for retrieving healthcare data.
. Parses JSON responses to extract clinic details.
. Saves the retrieved clinic data in an Excel file using Apache POI.
. Handles missing data fields such as phone numbers and websites by filling them with "N/A".
. Filters out duplicate clinic entries.
# Technologies Used
. Java: Core language used to build the project.
. Maven: Build automation tool and dependency management.
. Geoapify API: External API used to get clinic data.
. Apache POI: Java library for reading and writing Excel files.
. JSON (org.json): For parsing JSON responses.
# Project Structure
. src/main/java: Contains the Java code for the project.
. Gpclinics.java: Main class that fetches data from the API and saves it to an Excel file.
. GPClinicData.xlsx: Output Excel file containing the clinic data.
# Setup and Instructions
# Prerequisites
JDK (Java Development Kit) installed.
Maven installed for dependency management.
An API key from Geoapify to access their healthcare data.
Add your API key to the API_KEY variable in the Gpclinics.java file.

# The output will be saved in an Excel file (GPClinicData.xlsx)

# Conclusion
The  GP Data collector project demonstrates skills in API integration, JSON parsing, and data handling using Java and Maven. It serves as a helpful tool for anyone looking to retrieve and organize healthcare facility information in a specific region.
