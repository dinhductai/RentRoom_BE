package com.cntt.rentalmanagement.repository.impl;

import com.cntt.rentalmanagement.domain.models.Request;
import com.cntt.rentalmanagement.domain.models.Room;
import com.cntt.rentalmanagement.repository.BaseRepository;
import com.cntt.rentalmanagement.repository.RequestCustomRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Repository
public class RequestCustomRepositoryImpl implements RequestCustomRepository {
    @PersistenceContext
    private EntityManager em;

    private static final String FROM_REQUEST = " from rental_home.request r ";
    private static final String INNER_JOIN_ROOM = " inner join rental_home.room ro on r.room_id  = ro.id ";

    @Override
    public Page<Request> searchingOfRequest(String keyword,Long userId, Pageable pageable) {
        StringBuilder strQuery = new StringBuilder();
        strQuery.append(FROM_REQUEST);
        strQuery.append(INNER_JOIN_ROOM);
        strQuery.append(" where 1=1");
        Map<String, Object> params = new HashMap<>();
        if (!keyword.isEmpty() && Objects.nonNull(keyword)){
            strQuery.append(" AND (r.name like :keyword OR ro.title like :keyword) ");
            params.put("keyword", "%"+keyword+"%");
        }

        if (Objects.nonNull(userId)) {
            strQuery.append(" AND ro.user_id = :userId");
            params.put("userId", userId);
        }

        String strSelectQuery = "SELECT * " + strQuery;

        String strCountQuery = "SELECT COUNT(DISTINCT r.id)" + strQuery;

        return BaseRepository.getPagedNativeQuery(em,strSelectQuery, strCountQuery, params, pageable, Request.class);
    }

    @Override
    public Page<Request> searchingOfRequest(String keyword, String phone, Pageable pageable) {
        StringBuilder strQuery = new StringBuilder();
        strQuery.append(FROM_REQUEST);
        strQuery.append(INNER_JOIN_ROOM);
        strQuery.append(" where 1=1");
        Map<String, Object> params = new HashMap<>();
        if (!keyword.isEmpty() && Objects.nonNull(keyword)){
            strQuery.append(" AND (r.name like :keyword OR ro.title like :keyword) ");
            params.put("keyword", "%"+keyword+"%");
        }

        if (Objects.nonNull(phone) && !phone.isEmpty()) {
            strQuery.append(" AND r.phone_number = :phone");
            params.put("phone", phone);
        }

        String strSelectQuery = "SELECT * " + strQuery;

        String strCountQuery = "SELECT COUNT(DISTINCT r.id)" + strQuery;

        return BaseRepository.getPagedNativeQuery(em,strSelectQuery, strCountQuery, params, pageable, Request.class);

    }
}
