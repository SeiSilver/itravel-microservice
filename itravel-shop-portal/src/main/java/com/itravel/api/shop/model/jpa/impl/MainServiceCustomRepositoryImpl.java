package com.itravel.api.shop.model.jpa.impl;

import com.itravel.api.shop.model.entity.MainServiceEntity;
import com.itravel.api.shop.model.jpa.MainServiceCustomRepository;
import com.itravel.api.shop.payload.FilterRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.List;

@RequiredArgsConstructor
@Repository
public class MainServiceCustomRepositoryImpl implements MainServiceCustomRepository {
    private final EntityManager entityManager;

    @Override
    public List<MainServiceEntity> filter(FilterRequest request) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<MainServiceEntity> criteriaQuery = criteriaBuilder.createQuery(MainServiceEntity.class);

        Root<MainServiceEntity> root = criteriaQuery.from(MainServiceEntity.class);
        Predicate titlePre = criteriaBuilder.like(root.get("title"), "%" + request.getTitle() +"%");
        CriteriaBuilder.In<Integer> inCategoryId = criteriaBuilder.in(root.get("category").get("id"));
        for(Integer id : request.getCategoryIds())
            inCategoryId.value(id);
        criteriaQuery.where(titlePre, inCategoryId);

        if(request.getSortType() != null)
            switch (request.getSortType()){
                case 1: criteriaQuery.orderBy(criteriaBuilder.asc(root.get("lowestPrice"))); break;
                case 2: criteriaQuery.orderBy(criteriaBuilder.desc(root.get("lowestPrice"))); break;
            }

        TypedQuery<MainServiceEntity> query = entityManager.createQuery(criteriaQuery).setFirstResult((request.getPage()-1)*request.getSize()).setMaxResults(request.getSize());
        return query.getResultList();
    }
}
