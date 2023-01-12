package com.dreamtown.onasistownhouse.viewmodel;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class ViewModelUser {
    private Integer idUser;
    private String username;
    private String password;
    // private String email;
    private Integer role;
}
