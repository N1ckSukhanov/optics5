package com.app.optics.services;

import com.app.optics.models.Sidebar;
import com.app.optics.repositories.SidebarRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SidebarService {
    private final SidebarRepository sidebarRepository;

    public Sidebar get(){
        if (sidebarRepository.findAll().isEmpty()){
            sidebarRepository.save(new Sidebar());
        }
        return sidebarRepository.findAll().getFirst();
    }

    public void save(Sidebar sidebar){
        sidebarRepository.save(sidebar);
    }
}
