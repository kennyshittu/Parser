package com.ef.utils;

import static com.ef.utils.ParserUtil.LOG_DATE_FORMAT;

import com.ef.models.LogRow;
import java.io.FileReader;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

@Slf4j
public class ParserFileReader {

    private static final char PIPE = '|';
	public static List<LogRow> readCsvFile(String filePath) {

		//Create the CSVFormat object with the delimiter
        CSVFormat csvFileFormat = CSVFormat.DEFAULT.withDelimiter(PIPE);

        //Create a new list of log rows to be filled by CSV file data
        List<LogRow> logRows = new ArrayList<LogRow>();

        try (FileReader fileReader = new FileReader(filePath); CSVParser csvFileParser = new CSVParser(fileReader,
                csvFileFormat)){

            //Get a list of CSV file records
            List<CSVRecord> csvRecords = csvFileParser.getRecords();

            DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");

            //Read the CSV file records starting from the first record since there's no header
            for (int i = 0; i < csvRecords.size(); i++) {
            	CSVRecord record = csvRecords.get(i);
                LogRow logRow = new LogRow();

                logRow.setStartdate(LOG_DATE_FORMAT.parse(record.get(0)));
                logRow.setIp(record.get(1));
                logRow.setRequest(record.get(2));
                logRow.setStatus(Integer.parseInt(record.get(3)));
                logRow.setUseragent(record.get(4));

//                System.out.println("record : " + logRow);

                logRows.add(logRow);
			}

        } catch (Exception e) {
        	log.error("Error in ParserFileReader !!!", e);
        }
        return logRows;
	}

}
