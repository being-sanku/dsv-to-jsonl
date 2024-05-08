package com.api;

import com.conversionService.ConversionService;

public class Main {
    public static void main(String args[]) {


        try {
            String inputFileName = args[0];
            char delimeter = args[1].charAt(0);
            String outputFileName = args[2];

            FormatConverter converter = new DsvToJSONLConverter();
            ConversionService service = new ConversionService(converter);

            service.convertFile(inputFileName,delimeter,outputFileName);


        }
        catch (Exception e) {
            System.out.println("Something really went Wrong...");
            e.printStackTrace();
        }
    }
}
