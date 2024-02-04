package com.shopwa.category;

import com.shopwa.entity.Category;
import com.shopwa.exception.CategoryNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
public class CategoryService {
    @Autowired private CategoryRepository repo;

    public List<Category> listNoChildrenCategories() {
        List<Category> listNoChildrenCategories = new ArrayList<>();

        List<Category> listEnabledCategories = repo.findAllEnabled();

        listEnabledCategories.forEach(category -> {
            Set<Category> children = category.getChildren();
            if (children == null || children.size()==0) {
                listNoChildrenCategories.add(category);
            }
        });
        return listNoChildrenCategories;
    }

    public Category getCategory(String alias) throws CategoryNotFoundException {
        Category category = repo.findByAliasEnabled(alias);
        if (category == null) {
            throw new CategoryNotFoundException("Could not find any category wit alias: " + alias);
        }
        return  category;
    }

    public List<Category> getCategoryParents(Category child) {
        List<Category> categories = new ArrayList<>();

        Category parent = child.getParent();
        while(parent != null) {
            categories.add(0, parent);
            parent = parent.getParent();
        }

        categories.add(child);

        return categories;
    }
}
