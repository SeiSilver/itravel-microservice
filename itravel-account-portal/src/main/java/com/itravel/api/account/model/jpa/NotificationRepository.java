package com.itravel.api.account.model.jpa;

import com.itravel.api.account.model.entity.NotificationEntity;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface NotificationRepository extends JpaRepository<NotificationEntity, Integer> {

  List<NotificationEntity> getAllByReceiverIdOrderByCreatedAtDesc(Long receiverId);

  @Query("SELECT n FROM NotificationEntity n "
      + "WHERE n.receiverId = 0 OR n.receiverId = ?1")
  List<NotificationEntity> getAllByReceiverIdForModAndAdmin(Long receiverId);

}
