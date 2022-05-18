package com.itravel.api.account.model.jpa;

import com.itravel.api.account.model.entity.SellerInfoEntity;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SellerInfoRepository extends JpaRepository<SellerInfoEntity, Long> {

  Optional<SellerInfoEntity> findByAccountId(Long accountId);

  Page<SellerInfoEntity> findAllByIsVerifiedIsNullOrIsVerifiedFalse(Pageable pageable);

}
