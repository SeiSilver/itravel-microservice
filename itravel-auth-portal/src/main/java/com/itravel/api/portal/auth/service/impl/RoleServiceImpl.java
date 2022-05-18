package com.itravel.api.portal.auth.service.impl;

import com.itravel.api.portal.auth.model.dto.RoleInfo;
import com.itravel.api.portal.auth.model.jpa.RoleRepository;
import com.itravel.api.portal.auth.service.RoleService;
import com.itravel.api.portal.auth.util.ModelMapperUtils;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

@Service
@Log4j2
@AllArgsConstructor
public class RoleServiceImpl implements RoleService {

  private final RoleRepository roleRepository;
  private final ModelMapperUtils modelMapperUtils;

  @Override
  public List<RoleInfo> getRoleByAccountId(Long accountId) {
    return modelMapperUtils.mapList(roleRepository.findRolesByAccountId(accountId), RoleInfo.class);
  }
}
