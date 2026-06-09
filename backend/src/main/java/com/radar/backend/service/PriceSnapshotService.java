package com.radar.backend.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.radar.backend.model.entity.PriceSnapshot;
import com.radar.backend.repository.PriceSnapshotRepo;

@Service
public class PriceSnapshotService {
    
    @Autowired
    private PriceSnapshotRepo priceSnapshotRepo;

    public List<PriceSnapshot> getAll() {
        return priceSnapshotRepo.findAll();
    }

}
