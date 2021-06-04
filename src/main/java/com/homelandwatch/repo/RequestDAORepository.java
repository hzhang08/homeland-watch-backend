package com.homelandwatch.repo;

import com.homelandwatch.model.RequestDAO;

import java.util.List;

public interface RequestDAORepository {
    List<RequestDAO> findAll();

    void save(RequestDAO requestDAO);

    RequestDAO findById(long id);

    void deleteById(long id);

    List<RequestDAO> listAllElderlyRequests(int elderlyId);

    List<RequestDAO> listMyAcceptedRequets(int volunteerId);

    void accept(int requestId, int volunteerId);
}
