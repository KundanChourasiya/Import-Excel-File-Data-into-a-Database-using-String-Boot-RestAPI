package com.demo.Service;

import com.demo.Entity.Product;
import com.demo.Repository.ProductRepository;
import org.apache.poi.ss.usermodel.*;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedList;
import java.util.List;

@Service
public class ImportService {

    private ProductRepository repository;

    public ImportService(ProductRepository repository) {
        this.repository = repository;
    }

    // create import method
    public void importExcelToDatabase(InputStream file) throws IOException {

        LinkedList<Product> productList = new LinkedList<>();

        // open excel file
        Workbook workbook = WorkbookFactory.create(file);

        // select worksheet in excel file
        Sheet sheet = workbook.getSheetAt(0);

        sheet.forEach(row -> {
            Product product = new Product();

            // skip first row in excel file
            if (row.getRowNum() > 0) {

                // get data from cell 1
                product.setProduct_no((int) row.getCell(0).getNumericCellValue());

                // get data from cell 2
                product.setProduct_name(row.getCell(1).getStringCellValue());

                // get data from cell 3
                product.setProduct_price((long) row.getCell(2).getNumericCellValue());

                // get data from cell 4
                product.setQuantity((int) row.getCell(3).getNumericCellValue());

                // get data from cell 5
                product.setCategory(row.getCell(4).getStringCellValue());

                // add all data into linkList
                productList.add(product);
            }

        });

        // save all data into Database
        repository.saveAll(productList);
    }

    // Find all method to retrieve all data from database
    public List<Product> findAll() {
        List<Product> productList = repository.findAll();
        return productList;
    }

}
