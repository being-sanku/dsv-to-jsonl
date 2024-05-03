package com.conversionService;

import com.api.FormatConverter;

public class ConversionService {

    private FormatConverter converter;

    public ConversionService(FormatConverter converter) {
        this.converter = converter;
    }

    public void convertFile(String inputFileName,char delimiter, String outputFileName) throws Exception {
        converter.convert(inputFileName,
                delimiter,
                outputFileName);
    }


}
