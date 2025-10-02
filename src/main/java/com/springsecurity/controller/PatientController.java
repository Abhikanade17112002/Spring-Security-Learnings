package com.springsecurity.controller;

import com.springsecurity.dto.AppointmentResponseDto;
import com.springsecurity.dto.CreateAppointmentRequestDto;
import com.springsecurity.dto.PatientResponseDto;
import com.springsecurity.service.AppointmentService;
import com.springsecurity.service.PatientService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/patients")
@RequiredArgsConstructor
public class PatientController {

    private final PatientService patientService;
    private final AppointmentService appointmentService;

    @PostMapping("/appointments")
    public ResponseEntity<AppointmentResponseDto> createNewAppointment(@RequestBody CreateAppointmentRequestDto createAppointmentRequestDto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(appointmentService.createNewAppointment(createAppointmentRequestDto));
    }

    @GetMapping("/profile")
    private ResponseEntity<String> getPatientProfile() {
        Long patientId = 4L;
//        return ResponseEntity.ok(patientService.getPatientById(patientId));
        return ResponseEntity.ok("patientService.getPatientById(patientId)");
    }

}
