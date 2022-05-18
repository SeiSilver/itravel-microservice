package com.itravel.api.portal.auth.model.jpa;

import com.itravel.api.portal.auth.enums.RoleType;
import com.itravel.api.portal.auth.model.entity.AccountEntity;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountRepository extends JpaRepository<AccountEntity, Long>, AccountCustomRepository {

  Optional<AccountEntity> findByEmail(String email);

  List<AccountEntity> findByIdIn(List<Long> ids);

  @Query("SELECT DISTINCT A FROM AccountEntity A JOIN A.roles R "
      + "WHERE R.roleName in :roles "
      + "ORDER BY A.id ASC")
  Page<AccountEntity> findAllByRolesInOrderByIdAsc(Pageable pageable, @Param("roles") Collection<RoleType> roles);

  @Query("SELECT DISTINCT A FROM AccountEntity A JOIN A.roles R "
      + "WHERE R.roleName in :roles and "
      + "(A.email LIKE %:query% OR A.fullName LIKE %:query%) "
      + "ORDER BY A.id ASC")
  Page<AccountEntity> findAllByRolesInAndQuery(
      @Param("roles") Collection<RoleType> roles, @Param("query") String query, Pageable pageable);

}
