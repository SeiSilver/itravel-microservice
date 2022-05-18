package com.itravel.api.portal.auth.model.jpa;

import com.itravel.api.portal.auth.enums.RoleType;
import com.itravel.api.portal.auth.model.entity.RoleEntity;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<RoleEntity, Integer> {

  Optional<RoleEntity> findByRoleName(RoleType name);

  @Query("SELECT a.roles FROM AccountEntity a WHERE a.id = ?1")
  List<RoleEntity> findRolesByAccountId(Long accountId);

}
