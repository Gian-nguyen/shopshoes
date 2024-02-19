package jp.gihyo.projava.tasklist;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.nio.file.Paths;

@Controller
public class HomeController {

    private final TaskListDao dao;

    @Autowired
    HomeController(TaskListDao dao) {
        this.dao = dao;
    }

    record TaskItem(String id, String task, boolean done, String number, String cost, String price, String picture) {}

    @RequestMapping(value = "/home")
    String hello(Model model) {
        model.addAttribute("time", LocalDateTime.now());
        return "BUTTON";
    }

//    @GetMapping("/order")
//    String showOrderPage(Model model) {
//        // Thực hiện bất kỳ xử lý cần thiết cho trang order.html ở đây (nếu cần)
//        return "order";
//    }

    @GetMapping("/product")
    String showRevenuePage(Model model) {
        // Thực hiện bất kỳ xử lý cần thiết cho trang order.html ở đây (nếu cần)
        return "redirect:/list";
    }

    @GetMapping("/register")
    String showContactPage(Model model) {
        // Thực hiện bất kỳ xử lý cần thiết cho trang order.html ở đây (nếu cần)
        return "home";
    }

    @GetMapping("/list")
    String listItems(Model model) {
        List<TaskItem> taskItems = dao.findAll();
        model.addAttribute("taskList", taskItems);
        return "product";
    }

    @PostMapping("/add")
    String addItem(@RequestParam("task") String task,
                   @RequestParam("picture") MultipartFile picture,
                   @RequestParam("number") String number,
                   @RequestParam("cost") String cost,
                   @RequestParam("price") String price) {
        String id = UUID.randomUUID().toString().substring(0, 8);

        // Lưu file vào thư mục images
        try {
            String uploadDir = "C:\\Users\\giang\\OneDrive\\Máy tính\\tasklist\\tasklist\\src\\main\\resources\\static\\images";
            Path uploadPath = Paths.get(uploadDir);

            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }

            // Lưu file vào thư mục images với tên gốc của file
            Path filePath = uploadPath.resolve(picture.getOriginalFilename());
            Files.copy(picture.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

            String picturePath = picture.getOriginalFilename();

            TaskItem item = new TaskItem(id, task, false, number, cost, price, picturePath);
            dao.add(item);
        } catch (IOException e) {
            e.printStackTrace();
            // Xử lý ngoại lệ khi lưu file không thành công
        }

        return "redirect:/list";
    }

    @PostMapping("/delete")
    String deleteItem(@RequestParam("id") String id) {
        dao.delete(id);
        return "redirect:/list";
    }

    @PostMapping("/update")
    String updateItem(@RequestParam("id") String id,
                      @RequestParam("task") String task,
                      @RequestParam("picture") String picture,
                      @RequestParam("done") boolean done,
                      @RequestParam("number") String number,
                      @RequestParam("cost") String cost,
                      @RequestParam("price") String price) {
        TaskItem taskItem = new TaskItem(id, task, done, number, cost, price, picture);
        dao.update(taskItem);
        return "redirect:/list";
    }
}
