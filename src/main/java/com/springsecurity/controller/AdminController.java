package com.springsecurity.controller;

import com.springsecurity.dto.AddRoleRequestDTO;
import com.springsecurity.dto.PatientResponseDto;
import com.springsecurity.entity.Authorities;
import com.springsecurity.entity.Roles;
import com.springsecurity.service.AdminService;
import com.springsecurity.service.PatientService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {

    private final PatientService patientService;

    @Autowired
    private AdminService adminService ;

    @GetMapping("/patients")
    public ResponseEntity<List<PatientResponseDto>> getAllPatients(
            @RequestParam(value = "page", defaultValue = "0") Integer pageNumber,
            @RequestParam(value = "size", defaultValue = "10") Integer pageSize
    ) {
        return ResponseEntity.ok(patientService.getAllPatients(pageNumber, pageSize));
    }


    @PostMapping("/addauthority")
    public ResponseEntity<Authorities> addNewAuthority(@RequestBody Authorities authoritie ){
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(
                        adminService.addNewAuthority( authoritie )
                ) ;
    }


    @PostMapping("/addrole")
    public ResponseEntity<Roles> addNewRole(@RequestBody AddRoleRequestDTO addRoleRequestDTO){
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(
                        adminService.addNewRole( addRoleRequestDTO)
                ) ;
    }

    @GetMapping("/authorities")
    public ResponseEntity<List<Authorities>> getAuthorties( ){
        return ResponseEntity.status(HttpStatus.OK)
                .body(
                        adminService.getAuthorities()
                ) ;
    }

    @GetMapping("/roles")
    public ResponseEntity<List<Roles>> getRoles( ){
        return ResponseEntity.status(HttpStatus.OK)
                .body(
                        adminService.getRoles()
                ) ;
    }
}
