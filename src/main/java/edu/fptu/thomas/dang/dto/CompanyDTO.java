package edu.fptu.thomas.dang.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;

@NoArgsConstructor
@Setter
@Getter
@ToString
public class CompanyDTO {
    private int id;
    private String name;
    private String country;
    private LocalDate foundationDate;
    private int capital;
    private boolean isHeadQuarter;


}
