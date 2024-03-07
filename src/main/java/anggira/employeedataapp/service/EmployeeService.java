package anggira.employeedataapp.service;

import anggira.employeedataapp.entity.Employee;
import anggira.employeedataapp.model.EmployeeModel;
import anggira.employeedataapp.model.InputRequest;
import anggira.employeedataapp.model.SearchRequest;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

public interface EmployeeService {
    List<EmployeeModel> getAll();
    void inputEmployee(InputRequest request);

    void updateEmployee(InputRequest request);

    void delete(String nik);

    List<EmployeeModel> search(String nik, String nama);

}
