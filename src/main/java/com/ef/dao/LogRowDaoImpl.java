package com.ef.dao;

import com.ef.models.LogRow;
import java.util.Date;
import java.util.List;
import javax.persistence.EntityManager;
import org.joda.time.DateTime;

@SuppressWarnings( "unchecked" )
public class LogRowDaoImpl implements LogRowDao {

    private EntityManager em;

    @Override
    public LogRow getById(long id) {
        return em.find(LogRow.class, id);
    }

    @Override
    public List<LogRow> getAll() {
        return em.createQuery("SELECT l FROM LogRow l").getResultList();
    }

    @Override
    public void save(LogRow logRow) {
        em.getTransaction().begin();
        em.persist(logRow);
        em.getTransaction().commit();
    }

    @Override
    public void bulkSave(List<LogRow> logRows) {
        em.getTransaction().begin();
        logRows.forEach(logRow -> {
            em.persist(logRow);
        });
        em.getTransaction().commit();
    }

    @Override
    public List<Object[]> findWithOptions(DateTime from, DateTime to, Long threshold) {
        return em.createQuery("SELECT l.ip AS ip, COUNT(l.startdate) AS total FROM LogRow l " +
                                                "WHERE l.startdate BETWEEN :from AND :to " +
                                                "GROUP BY l.ip ORDER BY l.ip ASC")
                .setParameter("from", from)
                .setParameter("to", to)
                .getResultList();

    }
}
