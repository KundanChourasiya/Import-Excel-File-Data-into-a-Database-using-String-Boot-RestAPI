package com.demo.Controller;

import com.demo.Entity.Product;
import com.demo.Service.ImportService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/product")
public class ImportController {

    private ImportService service;

    public ImportController(ImportService service) {
        this.service = service;
    }

    // URL: http://localhost:8080/customer/upload
    @PostMapping(value = "/upload", consumes = "multipart/form-data")
    public ResponseEntity<String> importExcelToDatabase(@RequestParam("file") MultipartFile file) throws IOException {

        // check file is excel file or not
        String filename = file.getOriginalFilename();
        if (file == null || file.isEmpty() || (filename == null || (!filename.toLowerCase().endsWith(".xls") && !filename.toLowerCase().endsWith(".xlsx")))) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Only Import Excel file");
        }
        service.importExcelToDatabase(file.getInputStream());
        return ResponseEntity.ok("Excel File Data saved into Database");
    }

    // URL: http://localhost:8080/customer/read-data
    @GetMapping("/read-data")
    public ResponseEntity<List<Product>> findAll() {
        List<Product> productList = service.findAll();
        return ResponseEntity.ok(productList);
    }

}
