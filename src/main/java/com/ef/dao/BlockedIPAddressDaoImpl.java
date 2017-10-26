package com.ef.dao;

import com.ef.models.BlockedIPAddress;
import java.util.List;
import javax.persistence.EntityManager;

public class BlockedIPAddressDaoImpl implements BlockedIPAddressDao {

  private EntityManager em = PersistenceManager.INSTANCE.getEntityManager();

  @Override
  public void bulkSave(List<BlockedIPAddress> blockedIPAddresses) {
    em.getTransaction().begin();
    blockedIPAddresses.forEach(blockedIPAddress -> {
      em.persist(blockedIPAddress);
    });
    em.getTransaction().commit();
  }

  @Override
  public void truncate() {
    em.getTransaction().begin();
    em.createNativeQuery("TRUNCATE TABLE log_row").executeUpdate();
    em.getTransaction().commit();
  }
}
