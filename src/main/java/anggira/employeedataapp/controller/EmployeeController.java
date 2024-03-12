package anggira.employeedataapp.controller;

import anggira.employeedataapp.entity.Employee;
import anggira.employeedataapp.model.EmployeeModel;
import anggira.employeedataapp.model.InputRequest;
import anggira.employeedataapp.repository.EmployeeRepository;
import anggira.employeedataapp.service.EmployeeService;
import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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

    @GetMapping({"/", "/index"})
    public String getEmployees(@ModelAttribute("inputStatus") String inputStatus, Model model){
        List<EmployeeModel> employeeModels = employeeService.getAll();
        model.addAttribute("employees", employeeModels);
        model.addAttribute("title", "index");
        model.addAttribute("nik", "");
        model.addAttribute("nama", "");
        if (!inputStatus.isBlank() && !inputStatus.isEmpty()){
            model.addAttribute("success", inputStatus);
        }
        return "index";
    }

    @GetMapping(path = "/add")
    public String addForm(Model model){
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd ");
        Date date = new Date();
        String today = dateFormat.format(date);
        model.addAttribute("title", "add");
        model.addAttribute("today", today.trim());
        return "add";
    }

    @PostMapping(
            path = "/add",
            consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE
    )
    public String add(InputRequest request, Model model, RedirectAttributes redirectAttributes){
        Boolean inputEmployee = employeeService.inputEmployee(request);
        if (inputEmployee == false){
            model.addAttribute("error", "nik sudah terdaftar");
            return "add";
        }
        redirectAttributes.addFlashAttribute("inputStatus", "Data berhasil ditambahkan");
        return "redirect:/";
    }

    @GetMapping(path = "/edit/{nik}")
    public String editForm(@PathVariable String nik, Model model){
        Employee employee = employeeRepository.findById(nik).orElse(null);
        if (Objects.isNull(employee)){
            return "index";
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
        model.addAttribute("employee", data);
        model.addAttribute("title", "edit");
        model.addAttribute("today", today.trim());
        return "edit";
    }

    @PostMapping(
            path = "/edit",
            consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE
    )
    public ModelAndView edit(@ModelAttribute InputRequest request, RedirectAttributes redirectAttributes){
        employeeService.updateEmployee(request);
        redirectAttributes.addFlashAttribute("editStatus", "Data berhasil diubah");
        return new ModelAndView("redirect:/");
    }

    @PostMapping(
            path = "/delete/{nik}",
            consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE
    )
    public ModelAndView delete(@PathVariable String nik, RedirectAttributes redirectAttributes){
        employeeService.delete(nik);
        redirectAttributes.addFlashAttribute("deleteStatus", "Data berhasil dihapus");
        return new ModelAndView("redirect:/");
    }

    @GetMapping(path = "/detail/{nik}")
    public String detailForm(@PathVariable String nik, Model model){
        Employee employee = employeeRepository.findById(nik).orElse(null);
        if (Objects.isNull(employee)){
            return "index";
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

        model.addAttribute("employee", data);
        model.addAttribute("title", "edit");
        return "detail";
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
