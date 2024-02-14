package com.shopwa.section;

import com.shopwa.entity.section.Section;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SectionRepository extends JpaRepository<Section, Integer>{
	// list sections by enabled status and sorted by order in ascending order
	List<Section> findAllByEnabledOrderBySectionOrderAsc(boolean enabled);
}
