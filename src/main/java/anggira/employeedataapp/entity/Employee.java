package anggira.employeedataapp.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import javax.print.attribute.standard.DateTimeAtCompleted;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "employees")
public class Employee {

    @Id
    @Column(name = "nik")
    private String nik;

    @Column(name = "nama_lengkap")
    private String namaLengkap;

    @Column(name = "jenisKelamin")
    private String jenisKelamin;

    @Column(name = "tanggal_lahir")
    private LocalDate tanggalLahir;

    @Column(name = "alamat")
    private String alamat;

    @Column(name = "negara")
    private String negara;
}
