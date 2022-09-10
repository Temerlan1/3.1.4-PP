package demo.controllers;


import demo.model.Role;
import demo.model.User;
import demo.service.RoleService;
import demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

//@RestController
//@RequestMapping("/api/admin/users")
public class AdminController {
    private UserService userService;
    private RoleService roleService;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    AdminController(UserService userService, RoleService roleService, PasswordEncoder passwordEncoder1){
        this.userService = userService;
        this.roleService=roleService;
        this.passwordEncoder = passwordEncoder1;
    }

    @GetMapping("/{id}")//МЕТОД ЮЗЕРА, А ОСТАЛЬНЫЕ ПЕРЕНЕСЕМ В AdminController
    public User getUser(@PathVariable("id") long id){ //Principal principal){//@PathVariable("id") long id){
        System.out.println("get user");
        return userService.findById(id);
    }

    @GetMapping()
    public List<User> getUsers(){
        System.out.println("all users");
        return userService.findAll();
    }

    @PostMapping()
    public User createUser(@RequestBody User user){
        System.out.println("Huynya");
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userService.saveUser(user);
        System.out.println("save");
        return user;
    }

    @PutMapping
    User update(@RequestBody User user){
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userService.saveUser(user);
        return user;
    }

    @DeleteMapping("/{id}")
    String delete(@PathVariable long id){
        userService.removeUserById(id);
        return "User with ID = "+id+" was deleted";
    }

    @PostMapping("/delete/{id}")
    public String userDelete(@PathVariable("id") long id){
        userService.findById(id).setRoles(Collections.emptyList());
        userService.removeUserById(id);
        return "redirect:/admin";
    }

    @GetMapping("/update/{id}")
    public String updateUserForm(@PathVariable("id") long id,Model model){
        model.addAttribute("user",userService.findById(id));
        model.addAttribute("roles",roleService.findAll());
        return "add";//"update";
    }

    @PostMapping("/{id}")
    public String updateUser(/*@Validated @RequestBody User user){//*/@ModelAttribute("user") User user,
                                                                      @RequestParam(value = "index", required = false) Long[] ids){
        System.out.println(user.getUsername());
        System.out.println(user.getLastName());
        System.out.println(user.getAge());
        System.out.println(user.getRoles());
       // if (ids != null) {
       //     for (Long roleId : ids) {
       //         user.addRole(roleService.findById(roleId));
       //     }
       // } else {
       //     user.addRole(roleService.findByName("ROLE_USER"));
       // }
        user.setPassword(userService.findById(user.getId()).getPassword());
        System.out.println(user.getRoles());
        //user.setPassword(passwordEncoder.encode(user.getPassword()));
        System.out.println(user.getPassword());
        userService.update(user);
        System.out.println();
        return "redirect:/admin";
    }

    @GetMapping("/add/{id}")
    public String addForm(@PathVariable("id") long id, Model model){
        model.addAttribute("user",userService.findById(id));
        return "add";
    }

  //  @PostMapping("/add/{id}/{name}")
  //  public String addRole(@PathVariable("id") long id,@PathVariable("name") String name){
  //      userService.findById(id).getRoles().add(repository.findByName(name));
  //      return "redirect:/admin";
  //  }
}
