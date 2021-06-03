package com.homelandwatch.repo;

import com.homelandwatch.model.RequestDAO;

import java.util.List;

public interface RequestDAORepository {
    List<RequestDAO> findAll();

    void save(RequestDAO requestDAO);

    RequestDAO findById(long id);

    void deleteById(long id);
}
