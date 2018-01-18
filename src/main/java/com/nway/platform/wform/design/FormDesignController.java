package com.nway.platform.wform.design;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("formDesign")
public class FormDesignController {

	@RequestMapping("toFieldCreateUI")
	public String toFieldCreateUI() {

		return "formDesign/createField";
	}

	@RequestMapping("saveFields")
	public void saveFields(@RequestBody List<Map<String, String>> fields) {

		System.out.println(fields);
	}
}
