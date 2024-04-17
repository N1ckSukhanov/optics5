package com.app.optics.services;

import com.app.optics.models.AppState;
import lombok.Data;
import org.springframework.stereotype.Service;

@Service
@Data
public class AppService {
    AppState appState = AppState.PRODUCTS;
}
