package com.java.loginReg.entities;

import java.util.Map;

public class DoctorLoadResponseDTO {
    private Long doctorId;
    private String name;
    private Map<String, DayLoadDTO> load;

    public DoctorLoadResponseDTO(Long doctorId, String name, Map<String, DayLoadDTO> load) {
        this.doctorId = doctorId;
        this.name = name;
        this.load = load;
    }

    public Long getDoctorId() {
        return doctorId;
    }

    public String getName() {
        return name;
    }

    public Map<String, DayLoadDTO> getLoad() {
        return load;
    }
}
