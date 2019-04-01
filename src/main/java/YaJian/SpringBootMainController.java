package YaJian;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSONObject;

@Controller
public class SpringBootMainController {

	// http://127.0.0.1:8888/index/123
	@RequestMapping("/index/{key}")
	public String index(Model model, @PathVariable String key) {
		JSONObject json = new JSONObject();
		int aaa = 100;
		{
			List<JSONObject> list = new ArrayList<JSONObject>();
			for (int i = aaa + 1; i <= aaa + 10; i++) {
				JSONObject jsoni = new JSONObject();
				jsoni.put("id", i);
				jsoni.put("name", "name_" + i);
				list.add(jsoni);
			}
			json.put("listTop", list);
			model.addAttribute("listTop", list);
		}
		{
			List<JSONObject> list = new ArrayList<JSONObject>();
			for (int i = aaa + 11; i <= aaa + 18; i++) {
				JSONObject jsoni = new JSONObject();
				jsoni.put("id", i);
				jsoni.put("name", "name_" + i);
				list.add(jsoni);
			}
			json.put("listLeft", list);
			model.addAttribute("listLeft", list);
		}
		{
			List<JSONObject> list = new ArrayList<JSONObject>();
			for (int i = aaa + 21; i <= aaa + 25; i++) {
				JSONObject jsoni = new JSONObject();
				jsoni.put("id", i);
				jsoni.put("name", "name_" + i);
				list.add(jsoni);
			}
			json.put("listRight", list);
			model.addAttribute("listRight", list);
		}
		{
			List<JSONObject> list = new ArrayList<JSONObject>();
			for (int i = aaa + 31; i <= aaa + 38; i++) {
				JSONObject jsoni = new JSONObject();
				jsoni.put("id", i);
				jsoni.put("name", "name_" + i);
				list.add(jsoni);
			}
			json.put("listBottom", list);
			model.addAttribute("listBottom", list);
		}
		model.addAttribute("list", json.toString());
		return "/bootstrapTest";
	}

	// http://127.0.0.1:8888/vuePage/123
	@RequestMapping("/vuePage/{key}")
	public ModelAndView vuePage(@PathVariable String key) {
		ModelAndView view = new ModelAndView();
		view.addObject("list", "欢迎进入HTML页面" + key);
		view.setViewName("vueTest");
		return view;
	}

}
