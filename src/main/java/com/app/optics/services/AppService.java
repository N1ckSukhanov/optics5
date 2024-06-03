package com.app.optics.services;

import com.app.optics.models.AppState;
import com.app.optics.models.Sidebar;
import lombok.Data;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

@Service
@Data
public class AppService {
    AppState appState = AppState.PRODUCTS;
    private final CustomerService customerService;
    private final SidebarService sidebarService;

    public String getModelAppState() {
        return appState.toString().toLowerCase();
    }

    public void setModelState(AppState state, Model model) {
        appState = state;
        model.addAttribute("search", "");
        model.addAttribute("current", customerService.getCurrent());
        model.addAttribute("appState", getModelAppState());
        System.out.println(sidebarService.get());
        model.addAttribute("sidebar", sidebarService.get());
    }
}
