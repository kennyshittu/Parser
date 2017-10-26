package com.ef.dao;

import com.ef.models.LogRow;
import java.util.List;
import org.joda.time.DateTime;

public interface LogRowDao {
    LogRow getById(long id);

    List<LogRow> getAll();

    void save(LogRow logRow);

    void bulkSave(List<LogRow> logRows);

    List<Object[]> findWithOptions(DateTime from, DateTime to, Long threshold);
}
