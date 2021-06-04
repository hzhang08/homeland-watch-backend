package com.homelandwatch.service;

import com.homelandwatch.model.RequestDAO;
import com.homelandwatch.repo.RequestDAORepository;
import com.homelandwatch.repo.RequestDAORepositoryImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RequestServiceImpl implements RequestService {
    @Autowired
    private RequestDAORepository repo;

    @Override
    public List<RequestDAO> listAll() {
        return repo.findAll();
    }

    @Override
    public void save(RequestDAO product) {
        repo.save(product);
    }

    @Override
    public RequestDAO get(long id) {
        return repo.findById(id);
    }

    @Override
    public void delete(long id) {
        repo.deleteById(id);
    }

    @Override
    public List<RequestDAO> listAllElderlyRequests(int elderlyId) {
        return repo.listAllElderlyRequests(elderlyId);
    }

    @Override
    public List<RequestDAO> listMyAcceptedRequets(int volunteerId) {
        return repo.listMyAcceptedRequets(volunteerId);
    }

    @Override
    public void accept(int requestId, int volunteerId) {
        repo.accept(requestId,volunteerId);
    }
}
