package com.homelandwatch.service;

import com.homelandwatch.model.RequestDAO;

import java.util.List;

public interface RequestService {
    List<RequestDAO> listAll();

    void save(RequestDAO product);

    RequestDAO get(long id);

    void delete(long id);

    List<RequestDAO> listAllElderlyRequests(int elderlyId);
    List<RequestDAO> listMyAcceptedRequets(int volunteerId);

    void accept(int requestId, int volunteerId);
}
