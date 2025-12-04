package com.cntt.rentalmanagement.repository.impl;

import com.cntt.rentalmanagement.domain.models.Follow;
import com.cntt.rentalmanagement.domain.models.Room;
import com.cntt.rentalmanagement.repository.BaseRepository;
import com.cntt.rentalmanagement.repository.FollowRepositoryCustom;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Repository
public class FollowRepositoryCustomImpl implements FollowRepositoryCustom {

    @PersistenceContext
    private EntityManager em;

    private static final String FROM_REQUEST = " from rental_home.follow f ";
    @Override
    public Page<Follow> getPageFollow(Long userId,Pageable pageable) {

        StringBuilder strQuery = new StringBuilder();
        strQuery.append(FROM_REQUEST);

        strQuery.append(" where 1=1");
        Map<String, Object> params = new HashMap<>();

        if (Objects.nonNull(userId)) {
            strQuery.append(" AND f.customer_id = :userId");
            params.put("userId", userId);
        }
        String strSelectQuery = "SELECT * " + strQuery;

        String strCountQuery = "SELECT COUNT(DISTINCT f.id)" + strQuery;
        return BaseRepository.getPagedNativeQuery(em,strSelectQuery, strCountQuery, params, pageable, Follow.class);

    }
}
