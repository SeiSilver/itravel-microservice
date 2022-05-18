package com.itravel.api.account.model.jpa;

import com.itravel.api.account.model.entity.RateScoreEntity;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface RateScoreRepository extends JpaRepository<RateScoreEntity, Integer> {

  Page<RateScoreEntity> findByMainServiceId(Pageable pageable, Integer mainServiceId);

  List<RateScoreEntity> findByMainServiceId(Integer mainServiceId);

  Optional<RateScoreEntity> findByAccountIdAndMainServiceId(Long accountId, Integer mainServiceId);

  @Query("SELECT SUM(r.ratePoint) FROM RateScoreEntity r where r.mainServiceId = :mainServiceId")
  Long findRateScoreSum(Integer mainServiceId);

  Long countByMainServiceId(Integer mainServiceId);
}
