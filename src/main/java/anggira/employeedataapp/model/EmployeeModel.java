package anggira.employeedataapp.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class EmployeeModel {
    private String nik;
    private String namaLengkap;
    private Integer umur;
    private String jenisKelamin;
    private String tanggalLahir;
    private String alamat;
    private String negara;
}
