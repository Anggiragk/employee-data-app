package anggira.employeedataapp.service.impl;

import anggira.employeedataapp.entity.Employee;
import anggira.employeedataapp.model.EmployeeModel;
import anggira.employeedataapp.model.InputRequest;
import anggira.employeedataapp.model.SearchRequest;
import anggira.employeedataapp.repository.EmployeeRepository;
import anggira.employeedataapp.service.EmployeeService;
import jakarta.persistence.criteria.Predicate;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class EmployeeServiceImpl implements EmployeeService {

    private final EmployeeRepository employeeRepository;

    private EmployeeModel toEmployeeModel(Employee employee) throws ParseException {
        String tanggalLahir = "-";
        int age = 0;
        if (Objects.nonNull(employee.getTanggalLahir())) {
            DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            LocalDate date = LocalDate.parse(employee.getTanggalLahir().toString(), inputFormatter);
            DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("dd-MMM-yyyy", Locale.ENGLISH);
            tanggalLahir = date.format(outputFormatter);

            int currentYear = LocalDate.now().getYear();
            int birthday = employee.getTanggalLahir().getYear();
            age = currentYear - birthday;
        }

        if (Objects.isNull(employee.getJenisKelamin())) employee.setJenisKelamin("-");
        if (Objects.isNull(employee.getAlamat())) employee.setAlamat("-");
        if (Objects.isNull(employee.getNegara())) employee.setNegara("-");
        return EmployeeModel.builder()
                .nik(employee.getNik())
                .namaLengkap(employee.getNamaLengkap())
                .umur(age)
                .jenisKelamin(employee.getJenisKelamin())
                .tanggalLahir(tanggalLahir)
                .alamat(employee.getAlamat())
                .negara(employee.getNegara())
                .build();
    }

    @Override
    public List<EmployeeModel> getAll() {
        List<Employee> employees = employeeRepository.findAll();
        List<EmployeeModel> employeeModels = employees.stream().map(employee -> {
            try {
                return toEmployeeModel(employee);
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }
        }).collect(Collectors.toList());
        return employeeModels;
    }

    @Override
    public void inputEmployee(InputRequest request){
        LocalDate tanggalLahir = null;
        if (!request.getTanggalLahir().isBlank()){
            tanggalLahir = LocalDate.parse(request.getTanggalLahir());
        }

        Employee employee = Employee.builder()
                .nik(request.getNik())
                .namaLengkap(request.getNamaLengkap().trim())
                .tanggalLahir(tanggalLahir)
                .jenisKelamin(request.getJenisKelamin())
                .alamat(request.getAlamat().trim())
                .negara(request.getNegara())
                .build();
        employeeRepository.save(employee);
    }

    @Override
    public void updateEmployee(InputRequest request) {
        LocalDate tanggalLahir = null;
        if (!request.getTanggalLahir().isBlank()){
            tanggalLahir = LocalDate.parse(request.getTanggalLahir());
        }
        if (Objects.nonNull(request.getAlamat())) request.setAlamat(request.getAlamat().trim());
        Employee employee = employeeRepository.findById(request.getNik()).orElse(null);
        employee.setNamaLengkap(request.getNamaLengkap());
        employee.setJenisKelamin(request.getJenisKelamin());
        employee.setTanggalLahir(tanggalLahir);
        employee.setAlamat(request.getAlamat());
        employee.setNegara(request.getNegara());
        employeeRepository.save(employee);
    }

    @Override
    public void delete(String nik) {
        Employee employee = employeeRepository.findById(nik).orElse(null);
        if (Objects.nonNull(employee)) employeeRepository.delete(employee);
    }

    @Override
    public List<EmployeeModel> search(String nik, String nama){
        Specification<Employee> specification = (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (Objects.nonNull(nik)){
                predicates.add(criteriaBuilder.like(root.get("nik"), "%"+nik+"%"));
            }

            if (Objects.nonNull(nama)){
                predicates.add(criteriaBuilder.like(root.get("namaLengkap"), "%"+nama+"%"));
            }

            return query.where(predicates.toArray(new Predicate[]{})).getRestriction();

        };
        List<Employee> employees = employeeRepository.findAll(specification);
        List<EmployeeModel> employeeModels = employees.stream().map(employee -> {
            try {
                return toEmployeeModel(employee);
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }
        }).collect(Collectors.toList());
        return employeeModels;
    }


}
