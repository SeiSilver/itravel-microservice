package com.itravel.api.portal.auth.service;

import com.itravel.api.portal.auth.model.dto.RoleInfo;
import java.util.List;

public interface RoleService {

  List<RoleInfo> getRoleByAccountId(Long accountId);

}
