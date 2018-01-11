package com.nway.platform.wform.component.impl.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

public interface CheckboxMapper {

	List<String> getValues(@Param("pageName") String pageName, String bizId);

	void save(@Param("pageName") String pageName, @Param("bizId") String bizId, @Param("values") Object value);

	void clear(@Param("pageName") String pageName, String bizId);
}
