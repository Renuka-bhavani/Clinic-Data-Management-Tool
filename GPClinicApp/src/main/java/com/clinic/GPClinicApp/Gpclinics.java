
package com.clinic.GPClinicApp;

import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashSet;
import java.util.Set;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.json.JSONArray;
import org.json.JSONObject;

public class Gpclinics {

    private static final String API_KEY = "c145d667dd724a8da6333d72f0970cd1"; // Replace with your actual API key
    private static final String OUTPUT_FILE = "GPClinicData.xlsx";

    public static void main(String[] args) {
        String locationQuery = "General Practice Clinics in Queensland";
        String jsonResponse = fetchGPClinics(locationQuery);

        // Print the JSON response to see what you received
        System.out.println("JSON Response: " + jsonResponse);

        if (jsonResponse != null) {
            saveDataToExcel(jsonResponse);
        } else {
            System.out.println("Failed to fetch clinic data.");
        }
    }

    private static String fetchGPClinics(String query) {
        // Construct URL for Geoapify API
        String urlString ="https://api.geoapify.com/v2/places?categories=building.healthcare&filter=rect:138.0,-29.0,154.0,-10.0&apiKey=c145d667dd724a8da6333d72f0970cd1";

 
        StringBuilder responseContent = new StringBuilder();

        try {
            URL url = new URL(urlString);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setConnectTimeout(5000);  // Set connection timeout
            connection.setReadTimeout(5000);     // Set read timeout

            int status = connection.getResponseCode();
            BufferedReader reader;
            if (status > 299) {
                reader = new BufferedReader(new InputStreamReader(connection.getErrorStream()));
            } else {
                reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            }

            String line;
            while ((line = reader.readLine()) != null) {
                responseContent.append(line);
            }
            reader.close();

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

        return responseContent.toString();
    }

    @SuppressWarnings("resource")
    private static void saveDataToExcel(String jsonResponse) {
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("GP Clinics");

        // Create header row
        String[] columns = { "Clinic Name", "Address", "Phone", "Website" };
        Row headerRow = sheet.createRow(0);
        for (int i = 0; i < columns.length; i++) {
            headerRow.createCell(i).setCellValue(columns[i]);
        }

        JSONObject jsonObject = new JSONObject(jsonResponse);
        
        // Use a Set to store unique clinic entries
        Set<String> uniqueClinics = new HashSet<>();

        // Parse JSON response and write data to Excel
        if (jsonObject.has("features")) {
            JSONArray results = jsonObject.getJSONArray("features");
            System.out.println("Number of clinics found: " + results.length());
            int rowIndex = 1; // Row index for data (header is at 0)

            for (int i = 0; i < results.length(); i++) {
                JSONObject clinic = results.getJSONObject(i);
                JSONObject properties = clinic.getJSONObject("properties");

                // Extract data with default values if not found
                String name = properties.optString("name", "N/A");
                String address = properties.optString("formatted", "N/A");
                String phone = properties.has("contact") ? properties.getJSONObject("contact").optString("phone", "N/A") : "N/A";
                String website = properties.optString("website", "N/A");
                
             // Combine clinic name and address to form a unique key
                String uniqueKey = name + address;

                // Check if the clinic is already added (i.e., duplicate check)
                if (!uniqueClinics.contains(uniqueKey)) {
                    // Add the clinic to the unique set
                    uniqueClinics.add(uniqueKey);

                // Print each clinic data to console for verification
                System.out.println("Clinic: " + name + ", " + address + ", Phone: " + phone + ", Website: " + website);

                Row row = sheet.createRow(rowIndex++);
                row.createCell(0).setCellValue(name);
                row.createCell(1).setCellValue(address);
                row.createCell(2).setCellValue(phone);
                row.createCell(3).setCellValue(website);
            }
            }

            // Autosize columns
            for (int i = 0; i < columns.length; i++) {
                sheet.autoSizeColumn(i);
            }

            // Write the file
            try (FileOutputStream fileOut = new FileOutputStream(OUTPUT_FILE)) {
                workbook.write(fileOut);
                System.out.println("Data saved to " + OUTPUT_FILE);
            } catch (Exception e) {
                e.printStackTrace();
            }
          }else {
            System.out.println("No results found.");
        }

        // Close workbook
        try {
            workbook.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    

}
}

