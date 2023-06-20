package com.yorijori.foodcode.controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.yorijori.foodcode.apidata.RecipeDataFetcher;
import com.yorijori.foodcode.jpa.entity.ApiRecipe;
import com.yorijori.foodcode.jpa.entity.Recipe;
import com.yorijori.foodcode.jpa.entity.UserInfo;
import com.yorijori.foodcode.service.ApiRecipeService;
import com.yorijori.foodcode.service.RecipeService;

@RequestMapping("/recipe")
@Controller
public class RecipeController {
	RecipeService recipeService;
	RecipeDataFetcher recipeDataFetcher;
	ApiRecipeService apiRecipeService;

	@Autowired
	public RecipeController(RecipeService recipeService, RecipeDataFetcher recipeDataFetcher,
			ApiRecipeService apiRecipeService) {
		super();
		this.recipeService = recipeService;
		this.recipeDataFetcher = recipeDataFetcher;
		this.apiRecipeService = apiRecipeService;
	}

	@RequestMapping("/QA")
	public String qaRecipe(Model model) {
		return "thymeleaf/recipe/recipeQA";
	}

	@RequestMapping("/view")
	public String viewRecipe(Model model) {
		return "thymeleaf/recipe/recipeview";
	}

	@RequestMapping("/list/{type}/{pageNo}")
	public String listRecipe(Model model, @PathVariable String type, @PathVariable int pageNo) throws IOException {
		if (type.equals("user")) {
			long count = recipeService.countAll();
			List<Recipe> list = recipeService.selectListByPage(pageNo, 9);
			model.addAttribute("count", count);
			model.addAttribute("type", type);
			model.addAttribute("list", list);
			model.addAttribute("pageNo", pageNo);
		} else {
			long count = apiRecipeService.countAll();
			List<ApiRecipe> list = apiRecipeService.getServerRecipe(pageNo, 9);
			model.addAttribute("count", count);
			model.addAttribute("type", type);
			model.addAttribute("list", list);
			model.addAttribute("pageNo", pageNo);
		}
		return "thymeleaf/recipe/recipelist";
	}

	@RequestMapping("/insert")
	public String insertRecipe(Model model) {
		return "thymeleaf/recipe/recipeInsert";
	}

	// server recipe detail view
	@RequestMapping("/view/server/{rcpSeq}")
	public String serverView(Model model, @PathVariable int rcpSeq) {
		ApiRecipe data = apiRecipeService.selectByRcpSeq(rcpSeq);
		model.addAttribute("data", data);
		return "thymeleaf/recipe/serverRecipeView";
	}

	// server recipe count
	@RequestMapping("/list/servercount")
	@ResponseBody
	public Long listCount(Model model) {
		long result = 0;
		result = apiRecipeService.countAll();
		return result;
	}

	/*
	 * @RequestMapping("/list/server/{pageNo}")
	 * 
	 * @ResponseBody public List<ApiRecipe> listApiRecipe(@PathVariable int pageNo)
	 * throws IOException { List<ApiRecipe> list =
	 * apiRecipeService.getServerRecipe(pageNo, 9); System.out.println("list : " +
	 * list); return list; }
	 */

	// DB저장용 평상시 사용 x
	@RequestMapping("/setting/{firstIdx}/{lastIdx}")
	public String setRecipeAPI(@PathVariable int firstIdx, @PathVariable int lastIdx) throws IOException {
		recipeDataFetcher.fetchRecipeData(firstIdx, lastIdx);
		return "thymeleaf/recipe/recipelist";
	}

	@RequestMapping("/like/{type}")
	@ResponseBody
	public String addWishList(@PathVariable String type, HttpSession session, int rcp_no) {
		String msg = "";
		UserInfo userinfo = (UserInfo) session.getAttribute("userInfo");
		if (type.equals("user")) {
			Recipe recipe = new Recipe();
			recipe.setRecipeNo(rcp_no);
			recipeService.wishList(userinfo, recipe);
		} else {
			ApiRecipe apirecipe = new ApiRecipe();
			apirecipe.setRcpSeq(rcp_no);
			apiRecipeService.wishList(userinfo, apirecipe);
		}
		return msg;
	}

}
