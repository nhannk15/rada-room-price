package com.radar.backend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.radar.backend.service.PriceSnapshotService;

@RestController
@RequestMapping("/api/price-snapshots")
public class PriceSnapshotController {
    
    @Autowired
    private PriceSnapshotService priceSnapshotService;

    @GetMapping
    public ResponseEntity<?> getAll() {
        return ResponseEntity.ok().body(priceSnapshotService.getAll());
    }

}
