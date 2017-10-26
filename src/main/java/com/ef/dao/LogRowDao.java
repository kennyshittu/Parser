package com.ef.dao;

import com.ef.models.LogRow;
import java.util.List;

public interface LogRowDao {
    LogRow getById(long id);

    List<LogRow> getAll();

    void save(LogRow logRow);

    void bulkSave(List<LogRow> logRows);

    List<LogRow> findWithOptions(String from, String to, Integer threshold);
}
