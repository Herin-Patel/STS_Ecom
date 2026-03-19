package com.ecom.controller;

import com.ecom.model.Category;
import com.ecom.service.CategoryService;

import jakarta.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;

import org.springframework.util.ObjectUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

@Controller
@RequestMapping("/admin")
public class AdminController {

	@Autowired
	private CategoryService categoryServiceObj;

	@GetMapping("/")
	public String index() {
		return "admin/index";
	}

	@GetMapping("/loadAddProduct")
	public String loadAddProduct() {
		return "admin/add_product";
	}

	@GetMapping("/category")
	public String category() {
		return "admin/category";
	}

	@PostMapping("/saveCategory")
	public String saveCategory(@ModelAttribute Category currentCategory, @RequestParam("file") MultipartFile file,
			HttpSession session) {

		try {

			String imageName = (file != null && !file.isEmpty()) ? file.getOriginalFilename() : "default.jpg";

			currentCategory.setImageName(imageName);

			if (categoryServiceObj.existCategory(currentCategory.getName())) {
				session.setAttribute("errorMssg", "Category name already exists !");
			} else {
				Category savedCategory = categoryServiceObj.saveCategory(currentCategory);

				if (ObjectUtils.isEmpty(savedCategory)) {
					session.setAttribute("errorMssg", "Not saved ! Internal server error.");
				} else {
					// File saveFile = new ClassPathResource("static/img").getFile();
					
					// Path path = Paths.get(saveFile.getAbsolutePath() + File.separator + "category_img" + File.separator + file.getOriginalFilename());
					
					// Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
					
					
					// Save image to folder
					String uploadDir = "C:/Users/herry/Desktop/STS_Workspace/Shopping_Cart/src/main/resources/static/img/category_img/";
									   //"C:\\Users\\herry\\Desktop\\STS_Workspace\\Shopping_Cart\\src\\main\\resources\\static\\img\\category_img"

					File dir = new File(uploadDir);
					if (!dir.exists())
						dir.mkdirs();
					
					Path path = Paths.get(uploadDir + imageName);
					Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);

					System.out.println(path);
					
					session.setAttribute("successMssg", "Category saved successfully.");
				}
			}
		} catch (Exception expObj) {
			System.out.println("Exception : ");
			expObj.printStackTrace();
			session.setAttribute("errorMssg", "Something went wrong.");
		}
		return "redirect:/admin/category";
	}
}
