package com.dreamtown.onasistownhouse.utils;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

@Service
public class TipeProperty {
    public List<String> getListTipeProperty() {
        List<String> listTipeProperty = new ArrayList<>();
        for (int i = 65 ; i <= 90; i++){
            listTipeProperty.add(new String(Character.toChars(i))) ;
        }
        return listTipeProperty;
    }
}
