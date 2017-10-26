package com.ef.commands;

import static com.ef.utils.ParserUtil.*;

import com.ef.dao.BlockedIPAddressDao;
import com.ef.dao.LogRowDao;
import com.ef.dao.PersistenceManager;
import com.ef.models.BlockedIPAddress;
import com.ef.models.LogRow;
import com.ef.utils.FileArgumentChecker;
import com.ef.utils.ParserFileReader;
import com.ef.utils.ParserUtil;
import io.dropwizard.cli.Command;
import io.dropwizard.setup.Bootstrap;
import java.io.File;
import java.net.URL;
import java.text.ParseException;
import java.util.*;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import net.sourceforge.argparse4j.inf.Namespace;
import net.sourceforge.argparse4j.inf.Subparser;
import org.apache.commons.lang3.StringUtils;

public class ParserCommand extends Command {
    private static final Integer HOURLY_THRESHOLD = 200;
    private static final Integer DAILY_THRESHOLD = 500;

    private Map<String, Integer> durationMap;
    private LogRowDao logRowDao;
    private BlockedIPAddressDao blockedIPAddressDao;

    @Inject
    public ParserCommand(LogRowDao logRowDao, BlockedIPAddressDao blockedIPAddressDao) {
        super("com.ef.Parser", "Parse log files");
        durationMap = new HashMap<>();
        durationMap.put("hourly", HOURLY_THRESHOLD);
        durationMap.put("daily", DAILY_THRESHOLD);
        this.logRowDao = logRowDao;
        this.blockedIPAddressDao = blockedIPAddressDao;
    }

    @Override
    public void configure(Subparser subparser) {
        // Add a command line option
        subparser.addArgument("-a", "--accesslog")
                .dest("file")
                .type(File.class)
                .choices(new FileArgumentChecker())
                .help("The access log file path");

        subparser.addArgument("-s", "--startDate")
                .dest("startDate")
                .type(String.class)
                .required(true)
                .help("The start startdate");

        subparser.addArgument("-d", "--duration")
                .dest("duration")
                .type(String.class)
                .choices(durationMap.keySet())
                .required(true)
                .help("The duration");

        subparser.addArgument("-t", "--threshold")
                .dest("threshold")
                .type(Integer.class)
                .required(true)
                .help("The threshold");
    }

    @Override
    public void run(Bootstrap<?> bootstrap, Namespace namespace) throws Exception {
        File logFile = namespace.get("file");
        String startDateString = namespace.getString("startDate");
        String duration = namespace.getString("duration");
        Integer threshold = namespace.getInt("threshold");
        Date startDate;

        // Validate Date and File.
        try {
            //Parse startdate string
            startDate = INPUT_DATE_FORMAT.parse(startDateString);

            //If accesslog is not provided, use default access log file.
            URL fileUrl = getClass().getClassLoader().getResource("access.log");
            if ( logFile == null && fileUrl != null ) {
                logFile = new File(fileUrl.getFile());
            }

        } catch ( ParseException e ) {
            String errorMsg = "Error: Invalid startdate format, accepted format is : " + INPUT_DATE_PATTERN;
            throw new IllegalArgumentException(errorMsg, e);
        }

        System.out.println("File : " + logFile.getName());
        System.out.println("startDateString : " + startDateString);
        System.out.println("startDate : " + startDate);
        System.out.println("duration : " + duration);
        System.out.println("threshold : " + threshold);


        List<LogRow> logRows = ParserFileReader.readCsvFile(logFile.getAbsolutePath());

                EntityManager em = PersistenceManager.INSTANCE.getEntityManager();
//        //
//                em.getTransaction().begin();
//                em.persist(logRows.get(0));
//                em.getTransaction().commit();

        // write log rows to db
//        logRows.forEach(logRow -> {
//            logRowDao.save(logRow);
//        });
//        logRowDao.bulkSave(logRows);

        // find with options
        int hours = duration.equals("daily") ? 24 : 1;

        String from = LOG_DATE_FORMAT.format(startDate);
        Date endDate = ParserUtil.addHours(startDate, hours);
        String to = LOG_DATE_FORMAT.format(endDate);

        System.out.println("From : " + from);
        System.out.println("To : " + to);

        List<LogRow> selectedLogRows=  em.createQuery(
                "SELECT l FROM LogRow l " +
                "WHERE l.startdate " +
                "BETWEEN :from AND :to " +
                "")
                .setParameter("from", startDate)
                .setParameter("to", endDate)
//                .setParameter("threshold", threshold)
                .getResultList();






//        List<LogRow> selectedLogRows = logRowDao.findWithOptions(from, to, threshold);
selectedLogRows.forEach(selectedLogRow -> {
    System.out.println("selected : " + selectedLogRow);
});
        System.out.println("selected : " + selectedLogRows);

//        //write found ip addresses to another table with comments
//        List<BlockedIPAddress> blockedIPAddresses = new ArrayList<>();
//        selectedLogRows.forEach(selectedLogRow -> {
//            BlockedIPAddress blockedIPAddress = new BlockedIPAddress();
//            blockedIPAddress.setIp(selectedLogRow.getIp());
//            blockedIPAddress.setComment(StringUtils.join("Blocked because over ", threshold,
//                            " requests were received from this IP in the last ", hours, "hours."));
//            blockedIPAddresses.add(blockedIPAddress);
//        });

        //bulk save
//        blockedIPAddressDao.bulkSave(blockedIPAddresses);
    }
}
