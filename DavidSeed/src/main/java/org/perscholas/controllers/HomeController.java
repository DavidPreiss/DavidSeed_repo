package org.perscholas.controllers;


import org.perscholas.dao.IUserRepo;
import org.perscholas.models.Course;
import org.perscholas.models.Student;
import org.perscholas.models.User;
import org.perscholas.services.CourseService;
import org.perscholas.services.StudentService;
import org.perscholas.services.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.ui.Model;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


@Controller
@SessionAttributes({"student","user"})
public class HomeController {

    private final StudentService studentService;
    private final UserService userService;
    private final CourseService courseService;
    private final IUserRepo userRepo;

    public HomeController(StudentService studentService, UserService userService, CourseService courseService, IUserRepo userRepo) {
        this.studentService = studentService;
        this.userService = userService;
        this.courseService = courseService;
        this.userRepo = userRepo;
    }


    @ModelAttribute("student")
    public Student student() {
        return new Student();
    }

    @ModelAttribute("user")
    public User user() {
        return new User();
    }

    @ModelAttribute("course")
    public Course course() {
        return new Course();
    }

    /*
            - controllers should be separated e.g. @RequestMapping("admin"), @RequestMapping("student")
            - provide as much as possible e.g. get/post/put/delete mappings
     */

    @GetMapping("/")
    public String home() {
        return "redirect:template";
    }

    //this is the home page
    @GetMapping("/template")
    public String template(){
        return "template";
    }



    @GetMapping("/findUser")
    public String findUser() {
        //model.addAttribute("allStudents", studentService.getAllStudent());
        return "findUser";
    }
    @GetMapping("/allStudents")
    public String allStudents(Model model) {
        model.addAttribute("allStudents", studentService.getAllStudent());
        return "allStudents";
    }


    @GetMapping("/allCourses")
    public String allCourses(Model model) {
        model.addAttribute("allCourses", courseService.getAllCourses());
        return "allCourses";
    }

    @GetMapping("/student")
    public String studentHome() {
        return "studentConfirmation";
    }

    @GetMapping("/user")
    public String userHome() {
        return "userConfirmation";
    }


    @GetMapping("/allUsers")
    public String allUsers(Model model) {
        model.addAttribute("allUsers", userService.getAllUser());
        return "allUsers";
    }



    @GetMapping("/userProfile")
    public String userProfile(@RequestParam("userEmail") String email, Model model)
    {

        User profileUser = userService.getUserByEmail(email);

        model.addAttribute("user", profileUser);
        return "userProfile";
    }

    @GetMapping("/deleteUser")
    public String deleteUser(@RequestParam("userEmail") String email)
    {

        User deletedUser = userService.getUserByEmail(email);
        userRepo.deleteById(deletedUser.getEmail());
        return "redirect:/allUsers";
    }
/*
    @PostMapping("/allUsers")
    public String allUsersDelete(@ModelAttribute("user") @Valid User user, BindingResult result, Model model)
    {
        User databaseUser = user;
        userService.deleteUser(user);
        model.addAttribute("user", user);
        return "redirect:/user/deleted";
    }
*/
    @GetMapping("/user/deleted")
    public String userDeleted() {
        return "userDeleted";
    }


    @GetMapping("/student/register")
    public String studentRegistration(@SessionAttribute("student") Student student){
        return "studentRegistration";
    }



    @PostMapping("/student/register")
    public String studentRegister(@ModelAttribute("student") @Valid Student student, BindingResult result, Model model) {
        System.out.println(result.hasErrors());
        if(result.hasErrors()) {
            return "studentRegistration";

        }else{
            Student databaseStudent = studentService.saveStudent(student);
            model.addAttribute("student", student);
            return "redirect:/student";
        }
    }


    @GetMapping("/user/register")
    public String userRegistration(@SessionAttribute("user") User user){
        return "userRegistration";
    }


    @PostMapping("/user/register")
    public String userRegister(@ModelAttribute("user") @Valid User user, BindingResult result, Model model) {
        System.out.println(result.hasErrors());
        if(result.hasErrors()) {
            return "userRegistration";

        }else{
            User databaseUser = userService.saveUser(user);
            model.addAttribute("user", user);
            return "redirect:/user";
        }
    }

    @GetMapping("/course/register")
    public String courseRegistration() {
        return "courseRegistration";
    }

    @PostMapping("/course/register")
    public String courseRegister(@ModelAttribute("course") @Valid Course course, BindingResult result, Model model) {
        System.out.println(result.hasErrors());
        if(result.hasErrors()) {
            return "courseRegistration";

        }else{
            System.out.println("Course ID: " + course.getId());
            Course newCourse = courseService.saveCourse(course);
            return "courseConfirmation";
        }
    }

    @GetMapping("/course/registerStudent")
    public String courseStudentRegistration(@SessionAttribute("student") Student student, Model model){

        if(student.getEmail() == null) {
            return "redirect:/student/login";
        }

        student = studentService.getStudentByEmail(student.getEmail());

        List<Course> allCourses = courseService.getAllCourses();
        ArrayList<Course> availableCourse = new ArrayList<>(allCourses.size());
        List<Course> studentCourses = student.getCourses();

        for(Course c : allCourses) {
            if(!studentCourses.contains(c)) {
                availableCourse.add(c);
            }
        }


        model.addAttribute("availableCourses", availableCourse);

        return "courseStudentSignup";
    }


    @PostMapping("/course/registerStudent")
    public String courseRegisterStudent(@SessionAttribute("student") Student student, @RequestParam Map<String, Long> allParams, Model model) {

        if(student.getEmail() == null) {
            return "redirect:student";
        }

        Student newKidInClass = studentService.getStudentByEmail(student.getEmail());

        for(Map.Entry<String, Long> entry : allParams.entrySet()) {
            System.out.println(entry.getKey() + " " + entry.getValue());
            newKidInClass.getCourses().add(courseService.getCourseById(entry.getValue()).get());
        }

        studentService.saveStudent(newKidInClass);

        return "redirect:student";
    }

    @PostMapping("/course/registerStudentAlt")
    public String courseRegisterStudentAlt(@RequestParam(name = "email") String email,
                                           @RequestParam(name = "courseID") long courseID)
    {

        if(courseService.getCourseById(courseID).isEmpty() || studentService.getStudentByEmail(email) == null)
        {
            return "redirect:/course/registerStudent";
        }
        Student newKidInClass = studentService.getStudentByEmail(email);

        newKidInClass.getCourses().add(courseService.getCourseById(courseID).get());

        studentService.saveStudent(newKidInClass);

        return "redirect:/student";

    }

    @GetMapping("getsession")
    public String getSession(){
    return "getsession";
    }



}
