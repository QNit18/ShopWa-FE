package com.shopwa.menu;

import com.shopwa.entity.Article;
import com.shopwa.entity.Menu;
import com.shopwa.entity.MenuType;
import com.shopwa.exception.MenuItemNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MenuService {
	@Autowired private MenuRepository repo;
	
	public List<Menu> getHeaderMenuItems() {
		return repo.findByTypeAndEnabledOrderByPositionAsc(MenuType.HEADER, true);
	}
	
	public List<Menu> getFooterMenuItems() {
		return repo.findByTypeAndEnabledOrderByPositionAsc(MenuType.FOOTER, true);
	}
	
	public Article getArticleBoundToMenu(String menuAlias) throws MenuItemNotFoundException {
		Menu menu = repo.findByAlias(menuAlias);
		if (menu == null) {
			throw new MenuItemNotFoundException("No menu found with alias " + menuAlias);
		}
		
		return menu.getArticle();
	}	
}
