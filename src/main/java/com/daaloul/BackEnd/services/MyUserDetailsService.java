package com.daaloul.BackEnd.services;

import com.daaloul.BackEnd.models.User;
import com.daaloul.BackEnd.models.UserPrincipal;
import com.daaloul.BackEnd.repos.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class MyUserDetailsService  implements UserDetailsService {


    @Autowired
    private UserRepo userRepo;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = (User) userRepo.findByEmail(username);
        if(user == null){
            throw new UsernameNotFoundException("raw ma famech user");

        }
        else {
            return new UserPrincipal(user);
        }
    }

//
//    @Autowired
//    private UserRepo userRepo;
//
//    @Override
//    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
//
//        Users user =  userRepo.findByUserName(username);
//        if (user==null){
//            System.out.println("user not found");
//            throw new UsernameNotFoundException("user not found");
//        }
//        return new UserPrincipal(user);
//    }
}
