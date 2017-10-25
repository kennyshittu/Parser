package com.ef.utils;

import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

@Slf4j
public class ParserFileReader {

    private static final char PIPE = '|';
	public static List<Map<String, String>> readCsvFile(String filePath) {

		//Create the CSVFormat object with the header mapping
        CSVFormat csvFileFormat = CSVFormat.DEFAULT.withDelimiter(PIPE).withSkipHeaderRecord();

        //Create a new list of org objects to be filled by CSV file data
        List<Map<String, String>> orgObjects = new ArrayList<Map<String, String>>();

        try (FileReader fileReader = new FileReader(filePath); CSVParser csvFileParser = new CSVParser(fileReader,
                csvFileFormat)){

            //Get a list of CSV file records
            List<CSVRecord> csvRecords = csvFileParser.getRecords();

            //Read the CSV file records starting from the second record to skip the header
            for (int i = 1; i < csvRecords.size(); i++) {
            	CSVRecord record = csvRecords.get(i);

                System.out.println("record : " + record);
			}

        } catch (Exception e) {
        	log.error("Error in ParserFileReader !!!", e);
        }
        return orgObjects;
	}

}
