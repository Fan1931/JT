package com.jt.service;

import com.jt.vo.EasyUITree;

import java.util.List;

public interface ItemCatService {
    String findItemCatName(Long itemCatId);

    List<EasyUITree> findEasyUITreeByParentId(Long parentId);

    List<EasyUITree> findItemCatListByCache(Long parentId);
}
