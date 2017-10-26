package com.ef.commands;

import static com.ef.utils.ParserUtil.INPUT_DATE_FORMAT;
import static com.ef.utils.ParserUtil.INPUT_DATE_PATTERN;

import com.ef.dao.BlockedIPAddressDao;
import com.ef.dao.LogRowDao;
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
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.inject.Inject;
import net.sourceforge.argparse4j.inf.Namespace;
import net.sourceforge.argparse4j.inf.Subparser;
import org.apache.commons.lang3.StringUtils;

public class ParserCommand extends Command {

  private LogRowDao logRowDao;
  private BlockedIPAddressDao blockedIPAddressDao;

  @Inject
  public ParserCommand(LogRowDao logRowDao, BlockedIPAddressDao blockedIPAddressDao) {
    super("parse", "Parse log files");
    this.logRowDao = logRowDao;
    this.blockedIPAddressDao = blockedIPAddressDao;
  }

  @Override
  public void configure(Subparser subparser) {
    //Optional command
    subparser.addArgument("-a", "--accesslog")
        .dest("file")
        .type(File.class)
        .choices(new FileArgumentChecker())
        .help("The access log file path (Optional, if not provided data is loaded from sample accesslog.)");

    subparser.addArgument("-s", "--startDate")
        .dest("startDate")
        .type(String.class)
        .required(true)
        .help("The start startdate");

    subparser.addArgument("-d", "--duration")
        .dest("duration")
        .type(String.class)
        .choices("daily", "hourly")
        .required(true)
        .help("The duration");

    subparser.addArgument("-t", "--threshold")
        .dest("threshold")
        .type(Integer.class)
        .required(true)
        .help("The threshold");

    //Optional command
    subparser.addArgument("-c", "--clean")
        .dest("clean")
        .type(Boolean.class)
        .setDefault(false)
        .help("A boolean flag to clean previously imported data. (Optional)");
  }

  @Override
  public void run(Bootstrap<?> bootstrap, Namespace namespace) throws Exception {

    File logFile = namespace.get("file");
    String startDateString = namespace.get("startDate");
    String duration = namespace.getString("duration");
    Integer threshold = namespace.getInt("threshold");
    Boolean clean = namespace.getBoolean("clean");
    Boolean load = namespace.getBoolean("load");
    Date startDate;

    // Validate Date and File.
    try {
      startDate = INPUT_DATE_FORMAT.parse(startDateString);
    } catch (ParseException e) {
      String errorMsg =
          "Error: Invalid startdate format, accepted format is : " + INPUT_DATE_PATTERN;
      throw new IllegalArgumentException(errorMsg, e);
    }

    if (clean) {
      // Trucate table to remove previously loaded log files.
      logRowDao.truncate();

      // Trucate table to remove previously blocked Addresses.
      blockedIPAddressDao.truncate();
    }

    if (logFile != null) { // if accesslog was specified read from accesslog.
      // Load logs from accesslog file
      List<LogRow> logRows = ParserFileReader.readCsvFile(logFile.getAbsolutePath());
      System.out.println(StringUtils.join("Loaded ", logRows.size(), " records from ",
          logFile.getName()));

      // write log rows to db
      System.out.println(StringUtils.join("About to bulk save ", logRows.size(),
          " logrow records. might take a while..."));
      logRowDao.bulkSave(logRows);
    }

    int hours = duration.equals("daily") ? 24 : 1;
    Date endDate = ParserUtil.addHours(startDate, hours);
    List<Object[]> results = logRowDao.findWithOptions(startDate, endDate);
    List<BlockedIPAddress> blockedIPAddresses = new ArrayList<>();

    // print query result
    for (Object[] result : results) {
      //Print IP addresses to screen
      String ip = (String) result[0];
      int count = ((Number) result[1]).intValue();

      if (count > threshold) {
        System.out.println(StringUtils.join("ip : ", ip, " count : ", count));
        //write found ip addresses to another table with comments
        BlockedIPAddress blockedIPAddress = new BlockedIPAddress();
        blockedIPAddress.setIp(ip);
        blockedIPAddress.setComment(StringUtils.join("Blocked because threshold ", threshold,
            " has been exceeded.", count, " requests were received from this IP in the last ",
            hours,
            " hours."));
        blockedIPAddresses.add(blockedIPAddress);
      }
    }

    // bulk save blocked Addresses.
    System.out.println(StringUtils.join("About to bulk save ", blockedIPAddresses.size(),
        " resulting ip and comments. might take a while..."));
    blockedIPAddressDao.bulkSave(blockedIPAddresses);
    System.out.println("Operation Completed!!!");
  }
}
