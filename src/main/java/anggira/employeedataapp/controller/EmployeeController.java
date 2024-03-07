package anggira.employeedataapp.controller;

import anggira.employeedataapp.entity.Employee;
import anggira.employeedataapp.model.EmployeeModel;
import anggira.employeedataapp.model.InputRequest;
import anggira.employeedataapp.repository.EmployeeRepository;
import anggira.employeedataapp.service.EmployeeService;
import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Controller
@AllArgsConstructor
public class EmployeeController {
    private final EmployeeService employeeService;
    private final EmployeeRepository employeeRepository;

    @GetMapping(path = "/")
    public ModelAndView getEmployees(Map<String, Object> model){
        List<EmployeeModel> employeeModels = employeeService.getAll();
        model.put("employees", employeeModels);
        model.put("title", "index");
        model.put("nik", "");
        model.put("nama", "");
        return new ModelAndView("index", model);
    }

    @GetMapping(path = "/add")
    public ModelAndView addForm(Map<String, Object> model){
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd ");
        Date date = new Date();
        String today = dateFormat.format(date).toString();
        model.put("title", "add");
        model.put("today", today);
        return new ModelAndView("add", model);
    }

    @PostMapping(
            path = "/add",
            consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE
    )
    public ModelAndView add(@ModelAttribute InputRequest request){
        employeeService.inputEmployee(request);
        return new ModelAndView("redirect:/");
    }

    @GetMapping(path = "/edit/{nik}")
    public ModelAndView editForm(@PathVariable String nik, Map<String, Object> model){
        Employee employee = employeeRepository.findById(nik).orElse(null);
        if (Objects.isNull(employee)){
            return new ModelAndView("redirect:/");
        }
        InputRequest data = new InputRequest();
        data.setNik(employee.getNik());
        data.setNamaLengkap(employee.getNamaLengkap());
        data.setTanggalLahir("-");
        data.setJenisKelamin("-");
        data.setAlamat("-");
        data.setNegara("-");

        if (Objects.nonNull(employee.getTanggalLahir())) data.setTanggalLahir(employee.getTanggalLahir().toString());
        if (Objects.nonNull(employee.getJenisKelamin())) data.setJenisKelamin(employee.getJenisKelamin());
        if (Objects.nonNull(employee.getAlamat())) data.setAlamat(employee.getAlamat());
        if (Objects.nonNull(employee.getNegara())) data.setNegara(employee.getNegara());

        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd ");
        Date date = new Date();
        String today = dateFormat.format(date).toString();
        model.put("employee", data);
        model.put("title", "edit");
        model.put("today", today);
        return new ModelAndView("edit", model);
    }

    @PostMapping(
            path = "/edit",
            consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE
    )
    public ModelAndView edit(@ModelAttribute InputRequest request){
        employeeService.updateEmployee(request);
        return new ModelAndView("redirect:/");
    }

    @PostMapping(
            path = "/delete/{nik}",
            consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE
    )
    public ModelAndView delete(@PathVariable String nik){
        employeeService.delete(nik);
        return new ModelAndView("redirect:/");
    }

    @GetMapping(path = "/detail/{nik}")
    public ModelAndView detailForm(@PathVariable String nik, Map<String, Object> model){
        Employee employee = employeeRepository.findById(nik).orElse(null);
        if (Objects.isNull(employee)){
            return new ModelAndView("redirect:/");
        }
        InputRequest data = new InputRequest();
        data.setNik(employee.getNik());
        data.setNamaLengkap(employee.getNamaLengkap());
        data.setTanggalLahir("-");
        data.setJenisKelamin("-");
        data.setAlamat("-");
        data.setNegara("-");

        if (Objects.nonNull(employee.getTanggalLahir())) data.setTanggalLahir(employee.getTanggalLahir().toString());
        if (Objects.nonNull(employee.getJenisKelamin())) data.setJenisKelamin(employee.getJenisKelamin());
        if (Objects.nonNull(employee.getAlamat())) data.setAlamat(employee.getAlamat());
        if (Objects.nonNull(employee.getNegara())) data.setNegara(employee.getNegara());

        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd ");
        Date date = new Date();
        String today = dateFormat.format(date).toString();
        model.put("employee", data);
        model.put("title", "edit");
        model.put("today", today);
        return new ModelAndView("detail", model);
    }

    @GetMapping(
            path = "/search"
    )
    public ModelAndView searchEmployees(
            @RequestParam(name = "nik") String nik,
            @RequestParam(name = "nama") String nama,
            Map<String, Object> model
    ){
        List<EmployeeModel> employeeModels = employeeService.search(nik, nama);
        model.put("employees", employeeModels);
        model.put("title", "index");
        model.put("nik", nik);
        model.put("nama", nama);
        return new ModelAndView("index", model);
    }
}
