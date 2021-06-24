package org.perscholas.controllers;


import lombok.extern.slf4j.Slf4j;
import org.perscholas.dao.IBracketRepo;
import org.perscholas.dao.ISeededPlayerRepo;
import org.perscholas.dao.IUserRepo;
import org.perscholas.models.*;
import org.perscholas.services.BracketService;
import org.perscholas.services.CourseService;
import org.perscholas.services.StudentService;
import org.perscholas.services.UserService;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.ui.Model;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Slf4j
@Controller
@SessionAttributes({"student","user"})
public class HomeController {

    private final StudentService studentService;
    private final UserService userService;
    private final CourseService courseService;
    private final BracketService bracketService;
    private final IUserRepo userRepo;
    private final IBracketRepo bracketRepo;
    private final ISeededPlayerRepo seededOrderRepo;

    public HomeController(StudentService studentService,
                          UserService userService,
                          CourseService courseService,
                          BracketService bracketService,
                          IUserRepo userRepo, IBracketRepo bracketRepo, ISeededPlayerRepo seededOrderRepo)
    {
        this.studentService = studentService;
        this.userService = userService;
        this.courseService = courseService;
        this.bracketService = bracketService;
        this.userRepo = userRepo;
        this.bracketRepo = bracketRepo;
        this.seededOrderRepo = seededOrderRepo;
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

    @ModelAttribute("bracket")
    public Bracket bracket() {
        return new Bracket();
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
    @GetMapping("/allBrackets")
    public String allBrackets(Model model) {
        model.addAttribute("allBrackets", bracketService.getAllBrackets());
        return "allBrackets";
    }




    @GetMapping("/userProfile")
    public String userProfile(@RequestParam("userEmail") String email, Model model)
    {

        User profileUser = userService.getUserByEmail(email);

        model.addAttribute("user", profileUser);
        return "userProfile";
    }
    @GetMapping("/makeMatch")
    public String makeMatch(@RequestParam(value = "bracketID") String bID, Model model)
    {

        Bracket profileBracket = bracketService.getBracketById(bID).get();
        //ArrayList<SeededPlayer> ;
        ArrayList<User> seededPlayerList = new ArrayList<>();// = profileBracket.getSeededList();
        while(seededPlayerList.size()<profileBracket.getSeededList().size())seededPlayerList.add(new User());
        for (User user : profileBracket.getSeededList())
        {

            seededPlayerList.set(
                    seededOrderRepo.getById(user.getEmail()+"+"+bID).getSeed()-1,
                    userRepo.getById(user.getEmail())
            );
        }
        profileBracket.setSeededList(seededPlayerList);
        String matchString = profileBracket.StringMatch();
        model.addAttribute("matchString", matchString);
        return "matchStringPage";
    }

    @GetMapping("/bracketProfile")
    public String bracketProfile(@RequestParam(value = "bracketID") String bID, Model model)
    {

        Bracket profileBracket = bracketService.getBracketById(bID).get();
        //ArrayList<SeededPlayer> ;
        ArrayList<User> seededPlayerList = new ArrayList<>();// = profileBracket.getSeededList();
        while(seededPlayerList.size()<profileBracket.getSeededList().size())seededPlayerList.add(new User());
        for (User user : profileBracket.getSeededList())
        {

            seededPlayerList.set(
                    seededOrderRepo.getById(user.getEmail()+"+"+bID).getSeed()-1,
                    userRepo.getById(user.getEmail())
                    );
        }
        profileBracket.setSeededList(seededPlayerList);
        model.addAttribute("bracket", profileBracket);
        return "bracketProfile";
    }
    @PostMapping("/AddNewUserToBracket")
    public String AddNewUserToBracket(@RequestParam("bracketID") String id,
                                      @RequestParam("userID")String email,
                                      @Param("seed")Integer seed,
                                      Model model)
    {
        log.warn(id + " " + email);
        //Can't add same user to multiple brackets for some reason
        if (seed==null || seed <1)
            bracketService.AddNewUserToBracket(id,email);
        else
            bracketService.AddNewUserToBracket(id,email,seed);
        Bracket profileBracket = bracketService.getBracketById(id).get();
        model.addAttribute("bracket",profileBracket);
        model.addAttribute("bracket",profileBracket);
        model.addAttribute("bracketID", id);
        //return "bracketProfile";
        return "redirect:/bracketProfile?bracketID="+id;

    }
    @PostMapping("/RemoveUserFromBracket")
    public String RemoveUserFromBracket(@RequestParam("bracketID") String id,
                                      @RequestParam("userEmail")String email,
                                      Model model)
    {
        log.warn(id + " " + email);

        bracketService.RemoveUserFromBracket(id,email);
        Bracket profileBracket = bracketService.getBracketById(id).get();
        model.addAttribute("bracket",profileBracket);
        model.addAttribute("bracket",profileBracket);
        model.addAttribute("bracketID", id);
        //return "bracketProfile";
        return "redirect:/bracketProfile?bracketID="+id;
    }


    @GetMapping("/deleteUser")
    public String deleteUser(@RequestParam("userEmail") String email)
    {

        User deletedUser = userService.getUserByEmail(email);
        userRepo.deleteById(deletedUser.getEmail());
        return "redirect:/allUsers";
    }
    @GetMapping("/deleteBracket")
    public String deleteBracket(@RequestParam("bracketID") String id)
    {
        //TO DO change to optional and redirect to a missing page if its not there.
        Bracket deletedBracket = bracketService.getBracketById(id).get();
        bracketRepo.deleteById(deletedBracket.getId());
        return "redirect:/allBrackets";
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
    public String userRegistration(){
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

    @GetMapping("/bracket/create")
    public String bracketCreation() {
        return "bracketCreation";
    }

    @PostMapping("/bracket/create")
    public String courseRegister(@ModelAttribute("bracket") @Valid Bracket bracket, BindingResult result, Model model) {
        System.out.println(result.hasErrors());
        if(result.hasErrors()) {
            return "bracketCreation";

        }else{
            System.out.println("Bracket ID: " + bracket.getId());
            Bracket newCourse = bracketService.saveBracket(bracket);
            return "bracketConfirmation";
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

    @GetMapping("/aboutMe")
    public String aboutMe(){
        return "aboutMe";
    }

}
