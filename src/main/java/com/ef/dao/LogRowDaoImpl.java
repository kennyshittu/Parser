package com.ef.dao;

import com.ef.models.LogRow;
import java.util.List;
import javax.persistence.EntityManager;

@SuppressWarnings("unchecked")
public class LogRowDaoImpl implements LogRowDao {

    private EntityManager em ;

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
    public List<LogRow> findWithOptions(String from, String to, Integer threshold) {
        return em.createQuery(
                "SELECT l, COUNT (l.ip) AS total FROM LogRow l " +
                "WHERE c.startdate " +
                "BETWEEN :from AND :to " +
                "GROUP BY l.ip " +
                "HAVING total >= :threshold")
                .setParameter("from", from)
                .setParameter("to", to)
                .setParameter("threshold", threshold)
                .getResultList();

    }
}
