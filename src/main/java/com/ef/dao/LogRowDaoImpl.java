package com.ef.dao;

import com.ef.models.LogRow;
import java.util.Date;
import java.util.List;
import javax.persistence.EntityManager;

@SuppressWarnings("unchecked")
public class LogRowDaoImpl implements LogRowDao {

  private EntityManager em = PersistenceManager.INSTANCE.getEntityManager();

  @Override
  public void bulkSave(List<LogRow> logRows) {
    em.getTransaction().begin();
    logRows.forEach(logRow -> {
      em.persist(logRow);
    });
    em.getTransaction().commit();
  }

  @Override
  public void truncate() {
    em.getTransaction().begin();
    em.createNativeQuery("TRUNCATE TABLE log_row").executeUpdate();
    em.getTransaction().commit();
  }

  @Override
  public List<Object[]> findWithOptions(Date from, Date to) {
    return em.createQuery("SELECT l.ip AS ip, COUNT(l.startdate) AS total FROM LogRow l " +
        "WHERE l.startdate BETWEEN :from AND :to GROUP BY l.ip ORDER BY l.ip ASC")
        .setParameter("from", from)
        .setParameter("to", to)
        .getResultList();
  }
}

