package com.ef.dao;

import com.ef.models.BlockedIPAddress;
import java.util.List;

public interface BlockedIPAddressDao {

  void bulkSave(List<BlockedIPAddress> blockedIPAddresses);

  void truncate();
}
