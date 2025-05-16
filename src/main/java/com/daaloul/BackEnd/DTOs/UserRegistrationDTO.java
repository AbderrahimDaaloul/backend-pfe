package com.daaloul.BackEnd.DTOs;

import com.daaloul.BackEnd.enums.Role;
import lombok.Data;

@Data
public class UserRegistrationDTO {
    private String name;
    private String email;
    private String password;
    private String phone;
    private Role role;
}
