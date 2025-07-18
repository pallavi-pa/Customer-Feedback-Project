package com.example.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import com.example.demo.model.Praising;
import com.example.demo.repository.PraisingRepository;

@Service
public class PraisingServiceImpl implements PraisingService {

    @Autowired
    private PraisingRepository praisingRepository;

    @Override
    public List<Praising> getAllPraising() {
        return praisingRepository.findAll();
    }
}
