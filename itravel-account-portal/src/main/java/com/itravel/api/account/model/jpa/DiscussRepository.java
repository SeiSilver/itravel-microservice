package com.itravel.api.account.model.jpa;

import com.itravel.api.account.model.entity.DiscussEntity;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DiscussRepository extends JpaRepository<DiscussEntity, Integer> {

  Optional<DiscussEntity> findByAccountIdAndMainServiceId(Long accountId, Integer mainServiceId);

  Page<DiscussEntity> findByMainServiceId(Pageable pageable, Integer mainServiceId);

  Optional<DiscussEntity> findByIdAndAccountId(Integer discussId, Long accountId);

}
