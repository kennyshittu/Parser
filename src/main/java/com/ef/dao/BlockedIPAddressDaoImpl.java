package com.ef.dao;

import com.ef.models.BlockedIPAddress;
import java.util.List;
import javax.persistence.EntityManager;

public class BlockedIPAddressDaoImpl implements BlockedIPAddressDao {
    private EntityManager em;

    @Override
    public void save(BlockedIPAddress blockedIPAddress) {
        em.getTransaction().begin();
        em.persist(blockedIPAddress);
        em.getTransaction().commit();
    }

    @Override
    public void bulkSave(List<BlockedIPAddress> blockedIPAddresses) {
        em.getTransaction().begin();
        blockedIPAddresses.forEach(blockedIPAddress -> {
            em.persist(blockedIPAddresses);
        });
        em.getTransaction().commit();
    }
}
