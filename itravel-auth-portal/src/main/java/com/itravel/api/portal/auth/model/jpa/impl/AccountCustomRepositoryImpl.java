package com.itravel.api.portal.auth.model.jpa.impl;

import com.itravel.api.portal.auth.payload.AccountUpdateRequest;
import com.itravel.api.portal.auth.model.entity.AccountEntity;
import com.itravel.api.portal.auth.model.jpa.AccountCustomRepository;
import java.time.ZonedDateTime;
import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaUpdate;
import javax.persistence.criteria.Root;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

@Repository
@RequiredArgsConstructor
public class AccountCustomRepositoryImpl implements AccountCustomRepository {

  private final EntityManager entityManager;

  @Override
  public void updateBasicInfo(Long id, AccountUpdateRequest dto) {
    Assert.notNull(id, "Id should not be null");
    Assert.notNull(dto, "Update object should not be null");

    CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
    CriteriaUpdate<AccountEntity> criteriaUpdate = criteriaBuilder
        .createCriteriaUpdate(AccountEntity.class);
    Root<AccountEntity> root = criteriaUpdate.from(AccountEntity.class);

    if (StringUtils.hasText(dto.getFullName())) {
      criteriaUpdate.set("fullName", dto.getFullName());
    }
    if (StringUtils.hasText(dto.getPhoneNumber())) {
      criteriaUpdate.set("phoneNumber", dto.getPhoneNumber());
    }
    if (dto.getGender() != null) {
      criteriaUpdate.set("gender", dto.getGender());
    }
    if (dto.getBirthday() != null) {
      criteriaUpdate.set("birthday", dto.getBirthday());
    }

    criteriaUpdate.set("modifiedAt", ZonedDateTime.now());
    criteriaUpdate.where(criteriaBuilder.equal(root.get("id"), id));
    entityManager.createQuery(criteriaUpdate).executeUpdate();

  }
}
