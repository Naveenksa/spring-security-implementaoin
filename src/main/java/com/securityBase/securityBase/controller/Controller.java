package com.securityBase.securityBase.controller;


import com.securityBase.securityBase.model.OurUser;
import com.securityBase.securityBase.repository.OurUserRepo;
import com.securityBase.securityBase.repository.ProductRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping
public class Controller {

    @Autowired
    private OurUserRepo ourUserRepo;
    @Autowired
    private ProductRepo productRepo;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping("/")
    public String goHome(){
        return "this is Public accessible with needing authentication";
    }

    @PostMapping("/user/save")
    public ResponseEntity<Object> saveUser(@RequestBody OurUser ourUser ){
        ourUser.setPassword(passwordEncoder.encode(ourUser.getPassword()));
        OurUser result=ourUserRepo.save(ourUser);
        if (result.getId()>0){
            return ResponseEntity.ok("user was saved");

        }
        return ResponseEntity.status(404).body("Error, User Not saved");

    }

    @GetMapping("/product/all")
    public ResponseEntity<Object> getAllProduct(){
        return ResponseEntity.ok(productRepo.findAll());
    }

    @GetMapping("user/all")
    @PreAuthorize("hasAuthority('ADMIN)")
    public ResponseEntity<Object> getAllUser(){
        return ResponseEntity.ok(ourUserRepo.findAll());
    }

    @GetMapping("/users/single")
    @PreAuthorize("hasAuthority('ADMIN) or hasAuthority('USER')")
    public ResponseEntity<Object> getMyDetails(){
        return ResponseEntity.ok(ourUserRepo.findByEmail(getLoggedInUserDetails().getUsername()));
    }


    public UserDetails getLoggedInUserDetails(){
        Authentication authentication= SecurityContextHolder.getContext().getAuthentication();
        if(authentication!=null && authentication.getPrincipal() instanceof UserDetails){
            return (UserDetails)  authentication.getPrincipal();
        }
   return null;
    }

}
