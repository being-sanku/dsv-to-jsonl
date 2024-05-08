import com.api.DsvToJSONLConverter;
import com.conversionService.ConversionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

public class DSVToJSONConverterTest {

    private ConversionService service;

    @BeforeEach
    public void serviceCreation() {
        service = new ConversionService(new DsvToJSONLConverter());
    }

    @Test
    public void input1Test() {
        String file1Path = "src/main/resources/DSV input 1.txt";
        String outputFilePath = "src/main/resources/generated_output_input1.jsonl";
        String correctOutputFilePath = "src/main/resources/output1.jsonl";

        try {
            service.convertFile(file1Path,',',outputFilePath);
            assertTrue(filesAreEqual(outputFilePath,correctOutputFilePath));
        }catch(Exception e) {
            fail("Exception while converting" + e.getMessage());
        }

    }
    @Test
    public void input2Test() {
        String file1Path = "src/main/resources/DSV input 2.txt";
        String outputFilePath = "src/main/resources/generated_output_input2.jsonl";
        String correctOutputFilePath = "src/main/resources/output2.jsonl";

        try {
            service.convertFile(file1Path,'|',outputFilePath);
            assertTrue(filesAreEqual(outputFilePath,correctOutputFilePath));
        }catch(Exception e) {
            fail("Exception while converting" + e.getMessage());
        }

    }
//    @Test
//    public void runConversionService() throws Exception {
//        String file1Path = "src/main/resources/input.csv";
//        String outputFilePath = "src/main/resources/output_1.jsonl";
//        char delimiter = ',';
//        service.convertFile(file1Path,delimiter,outputFilePath);
////        File output = new File()
//    }

    private boolean filesAreEqual(String file1, String file2) throws IOException {
        BufferedReader reader1 = new BufferedReader(new FileReader(file1));
        BufferedReader reader2 = new BufferedReader(new FileReader(file2));

        String line1 = reader1.readLine();
        String line2 = reader2.readLine();

        while (line1 != null && line2 != null) {
            if (!line1.equals(line2)) {
                reader1.close();
                reader2.close();
                return false;
            }
            line1 = reader1.readLine();
            line2 = reader2.readLine();
        }

        boolean result = (line1 == null && line2 == null);

        reader1.close();
        reader2.close();

        return result;
    }
}
