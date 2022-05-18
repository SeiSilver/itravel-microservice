package com.itravel.api.account.service.impl;

import com.itravel.api.account.exception.AccountNotFoundException;
import com.itravel.api.account.exception.DiscussNotFoundException;
import com.itravel.api.account.model.dto.AccountBasicInfo;
import com.itravel.api.account.model.dto.DiscussInfo;
import com.itravel.api.account.model.dto.DiscussViewInfo;
import com.itravel.api.account.model.dto.PagingEntityDto;
import com.itravel.api.account.model.entity.DiscussEntity;
import com.itravel.api.account.model.entitydto.AccountEntity;
import com.itravel.api.account.model.jpa.DiscussRepository;
import com.itravel.api.account.payload.DiscussCreateRequest;
import com.itravel.api.account.payload.DiscussUpdateRequest;
import com.itravel.api.account.payload.GetAccountsByIdsPayload;
import com.itravel.api.account.service.AccountService;
import com.itravel.api.account.service.DiscussService;
import com.itravel.api.account.util.ModelMapperUtils;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Log4j2
@RequiredArgsConstructor
public class DiscussServiceImpl implements DiscussService {

  private final DiscussRepository discussRepository;
  private final ModelMapperUtils modelMapperUtils;
  private final AccountService accountService;

  @Override
  public PagingEntityDto getDiscussByServiceId(Pageable pageable, Integer serviceId) throws DiscussNotFoundException, AccountNotFoundException {
    Page<DiscussEntity> pages = discussRepository.findByMainServiceId(pageable, serviceId);
    if (pages.isEmpty()) {
      throw new DiscussNotFoundException();
    }
    var accountIds = pages.getContent().stream()
        .map(DiscussEntity::getAccountId)
        .collect(Collectors.toList());
    GetAccountsByIdsPayload accountsByIdsPayload = GetAccountsByIdsPayload.builder().listId(accountIds).build();
    List<AccountBasicInfo> listAccount = accountService.getAccountByAccountIds(accountsByIdsPayload);
    var dto = modelMapperUtils.mapList(pages.getContent(), DiscussInfo.class);
    for (int i = 0; i < dto.size(); i++) {
      var temp = dto.get(i);
      for (var account : listAccount) {
        if (Objects.equals(account.getId(), temp.getAccountId())) {
          temp.setAccount(account);
          dto.set(i, temp);
          break;
        }
      }
    }

    List<DiscussViewInfo> discussViewInfos = new ArrayList<>();

    for (int i = 0; i < dto.size(); i++) {
      var core = dto.get(i);
      DiscussViewInfo temp = modelMapperUtils.map(core, DiscussViewInfo.class);
      var listReply = dto.stream().filter(n ->
          n.getDiscussParentId() != 0 &&
              !Objects.equals(n.getId(), core.getId()) &&
              Objects.equals(n.getDiscussParentId(), core.getId())
      ).collect(Collectors.toList());
      List<DiscussViewInfo> tempReply;
      if (!listReply.isEmpty()) {
        tempReply = modelMapperUtils.mapList(listReply, DiscussViewInfo.class);
        dto.removeAll(listReply);
      } else {
        tempReply = new ArrayList<>();
      }
      temp.setDiscussReply(tempReply);
      discussViewInfos.add(temp);
    }

    return PagingEntityDto
        .builder()
        .page(pages.getNumber() + 1)
        .totalResults(pages.getTotalElements())
        .totalPages(pages.getTotalPages())
        .results(Collections.singletonList(discussViewInfos))
        .build();
  }

  @Override
  @Transactional
  public DiscussInfo createDiscuss(AccountEntity accountVerified, DiscussCreateRequest discussCreateRequest) {
    DiscussEntity entity = DiscussEntity.builder()
        .id(0)
        .accountId(accountVerified.getId())
        .message(discussCreateRequest.getMessage())
        .mainServiceId(discussCreateRequest.getMainServiceId())
        .discussParentId(discussCreateRequest.getDiscussParentId())
        .build();
    var discussEntity = discussRepository.save(entity);
    var dto = modelMapperUtils.map(discussEntity, DiscussInfo.class);
    dto.setAccount(AccountBasicInfo.builder()
        .id(accountVerified.getId())
        .email(accountVerified.getEmail())
        .fullName(accountVerified.getFullName())
        .imageLink(accountVerified.getImageLink())
        .build());
    return dto;

  }

  @Override
  @Transactional
  public void updateDiscussByDiscussId(AccountEntity accountVerified, DiscussUpdateRequest discussUpdateRequest) throws DiscussNotFoundException {
    var discussEntity = discussRepository
        .findByIdAndAccountId(discussUpdateRequest.getId(), accountVerified.getId())
        .orElseThrow(DiscussNotFoundException::new);

    discussEntity.setMessage(discussUpdateRequest.getMessage());
    discussEntity.setDiscussParentId(discussUpdateRequest.getDiscussParentId());
    discussEntity.setMainServiceId(discussUpdateRequest.getMainServiceId());

    discussRepository.save(discussEntity);
  }

  @Override
  @Transactional
  public void deleteDiscussByDiscussId(AccountEntity accountVerified, Integer discussId) throws DiscussNotFoundException {
    var discussEntity = discussRepository
        .findByIdAndAccountId(discussId, accountVerified.getId())
        .orElseThrow(DiscussNotFoundException::new);
    discussRepository.delete(discussEntity);
  }

}
