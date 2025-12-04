package com.cntt.rentalmanagement.repository.impl;

import com.cntt.rentalmanagement.domain.models.Maintenance;
import com.cntt.rentalmanagement.repository.BaseRepository;
import com.cntt.rentalmanagement.repository.MaintenanceRepositoryCustom;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Repository
public class MaintenanceRepositoryCustomImpl implements MaintenanceRepositoryCustom {

    @PersistenceContext
    private EntityManager em;

    private static final String FROM_MAINTENANCE = " from rental_home.maintenance m  ";

    private static final String INNER_JOIN_ROOM = " inner join rental_home.room r on m.room_id = r.id ";


    @Override
    public Page<Maintenance> searchingMaintenance(String keyword,Long userId, Pageable pageable) {
        StringBuilder strQuery = new StringBuilder();
        strQuery.append(FROM_MAINTENANCE);
        strQuery.append(INNER_JOIN_ROOM);
        strQuery.append(" where 1=1");

        Map<String, Object> params = new HashMap<>();
        if(Objects.nonNull(keyword) && !keyword.isEmpty()) {
            strQuery.append(" AND r.title = :title");
            params.put("title", "%"+keyword+"%");
        }

        if (Objects.nonNull(userId)) {
            strQuery.append(" AND r.user_id = :userId");
            params.put("userId", userId);
        }


        String strSelectQuery = "SELECT * " + strQuery;

        String strCountQuery = "SELECT COUNT(DISTINCT m.id)" + strQuery;
        return BaseRepository.getPagedNativeQuery(em,strSelectQuery, strCountQuery, params, pageable, Maintenance.class);

    }

    @Override
    public BigDecimal sumPriceOfMaintenance(Long userId) {
        StringBuilder strQuery = new StringBuilder();
        strQuery.append(FROM_MAINTENANCE);
        strQuery.append(INNER_JOIN_ROOM);

        Map<String, Object> params = new HashMap<>();
        if (Objects.nonNull(userId)) {
            strQuery.append(" AND r.user_id = :userId");
            params.put("userId", userId);
        }

        String strSelectQuery = "SELECT sum(m.price) " + strQuery;

        Query query = em.createNativeQuery(strSelectQuery);
        params.forEach(query::setParameter);

        Integer sumPrice;
        try {
            sumPrice = ((Number) query.getSingleResult()).intValue();
        } catch (NoResultException e) {
            sumPrice = 0;
        }

        return BigDecimal.valueOf(sumPrice);
    }
}
