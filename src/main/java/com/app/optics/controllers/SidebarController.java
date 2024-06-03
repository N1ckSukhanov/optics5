package com.app.optics.controllers;

import com.app.optics.models.Sidebar;
import com.app.optics.services.SidebarService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import static com.app.optics.util.Constants.TO_PRODUCTS;

@Controller
@RequiredArgsConstructor
@RequestMapping("/sidebar")
public class SidebarController {
    private final SidebarService sidebarService;

    @GetMapping("/product")
    public String product(){
        Sidebar sidebar = sidebarService.get();
        sidebar.setProduct(!sidebar.isProduct());
        sidebarService.save(sidebar);
        return TO_PRODUCTS;
    }

    @GetMapping("/old")
    public String old(){
        Sidebar sidebar = sidebarService.get();
        sidebar.setOld(!sidebar.isOld());
        sidebarService.save(sidebar);
        return TO_PRODUCTS;
    }

    @GetMapping("/message")
    public String message(){
        Sidebar sidebar = sidebarService.get();
        sidebar.setMessage(!sidebar.isMessage());
        sidebarService.save(sidebar);
        return TO_PRODUCTS;
    }

    @GetMapping("/other")
    public String other(){
        Sidebar sidebar = sidebarService.get();
        sidebar.setOther(!sidebar.isOther());
        sidebarService.save(sidebar);
        return TO_PRODUCTS;
    }



}
