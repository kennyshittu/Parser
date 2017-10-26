package com.ef.dao;

import com.ef.models.LogRow;
import java.util.Date;
import java.util.List;

public interface LogRowDao {

  void bulkSave(List<LogRow> logRows);

  void truncate();

  List<Object[]> findWithOptions(Date from, Date to);
}
