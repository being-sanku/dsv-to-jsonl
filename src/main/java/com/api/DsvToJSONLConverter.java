package com.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.opencsv.CSVParser;
import com.opencsv.CSVParserBuilder;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;

import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;
public class DsvToJSONLConverter implements  FormatConverter {

    private static boolean isNumeric(String str) {
        return str.matches("-?\\d+(\\.\\d+)?");
    }
    private static boolean isInteger(String str) {
        return str.matches("-?\\d+");
    }
    private static boolean isValidDateFormat(String dateString, String dateFormat) {
        SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
        sdf.setLenient(false);
        try {
            Date date = sdf.parse(dateString);
            return true;

        }catch (ParseException e) {
            return false;
        }
    }

    private static boolean isValidDateFormat(String line) {

        String dateFormatRegexPattern = "(\\d{2}[-/]\\d{2}[-/]\\d{4})|(\\d{4}[-/]\\d{2}[-/]\\d{2})";
        Pattern dateFormat = Pattern.compile(dateFormatRegexPattern);
        Matcher matcher = dateFormat.matcher(line);
        return matcher.matches();
    }

    private static String dateFormatter(String line) {
        SimpleDateFormat inputFormat1 = new SimpleDateFormat("yyyy-dd-MM");
        SimpleDateFormat inputFormat2 = new SimpleDateFormat("dd-MM-yyyy");
        SimpleDateFormat inputFormat3 = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat inputFormat4 = new SimpleDateFormat("yyyy/MM/dd");

        Date date;
        SimpleDateFormat outputFormat  = new SimpleDateFormat("yyyy-MM-dd",Locale.ENGLISH);
        try {
            date = inputFormat1.parse(line);
        }catch(ParseException einput1) {
            try{

                date = inputFormat2.parse(line);
            }catch(Exception einput2){
                try {

                    date = inputFormat3.parse(line);
                }
                catch(ParseException einput3) {
                         try {
                        date = inputFormat4.parse(line);
                        }catch(ParseException einput4) {
                             return null;
                         }
                    }
                }
            }

        String formattedDate = outputFormat.format(date);



        return formattedDate;


    }
    @Override
    public  void convert(String inputFileName,char delimiter, String outputFileName) throws Exception{
            Path path = Paths.get(inputFileName);
            try(Reader reader = Files.newBufferedReader(path)) {
                CSVParser parser = new CSVParserBuilder().withSeparator(delimiter).build();
                try(CSVReader csvReader = new CSVReaderBuilder(reader).withSkipLines(0).withCSVParser(parser).build()) {

                    String[] headers = csvReader.readNext();
                    Stream<String[]> stream = csvReader.readAll().stream();

                    ObjectMapper objectMapper = new ObjectMapper();
//                    objectMapper.enable(SerializationFeature.);

                    try {
                        Files.deleteIfExists(Paths.get(outputFileName));
                        stream.forEach(line -> {
                            try {
                                Map<String,Object> data = new LinkedHashMap<>();
                                for(int i = 0; i < headers.length;i++) {

                                    if(isNumeric(line[i])) {
                                        if(isInteger(line[i])) {
                                            data.put(headers[i], Integer.parseInt(line[i]));
                                        }else {

                                            data.put(headers[i], Float.parseFloat(line[i]));
                                        }
                                    }else {
                                        if(isValidDateFormat(line[i])) {

                                            String formattedDate = dateFormatter(line[i]);
//                                            System.out.println(formattedDate);
                                            if(formattedDate != null) {
//
                                                data.put(headers[i],formattedDate);
                                            }
                                            else {
                                                data.put(headers[i], line[i]);
                                            }


                                        } else {
                                            data.put(headers[i], line[i]);
                                        }

                                    }
                                }
                                String json = objectMapper.writeValueAsString(data);
                                StringBuilder modifiedJson = new StringBuilder(json);
                                for (int i = 1; i < modifiedJson.length() - 1; i++) {
                                    if (modifiedJson.charAt(i) == ':' && modifiedJson.charAt(i + 1) != ' ') {
                                        modifiedJson.insert(i + 1, ' ');
                                    }
                                }
//                                System.out.println(json);
//                                Files.write(Paths.get(ossutputFileName),(json + System.lineSeparator()).getBytes(), java.nio.file.StandardOpenOption.CREATE, java.nio.file.StandardOpenOption.APPEND);
                                Files.write(
                                        Paths.get(outputFileName),
                                        (modifiedJson +System.lineSeparator()).getBytes(),
                                        java.nio.file.StandardOpenOption.CREATE,
                                        java.nio.file.StandardOpenOption.APPEND
                                );
                            }catch(Exception e) {
                                e.printStackTrace();
                            }
                        });
                    }catch (Exception e) {
                        e.printStackTrace();
                    }
                }

            }

    }


}
