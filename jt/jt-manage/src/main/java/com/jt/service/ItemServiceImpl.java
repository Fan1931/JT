package com.jt.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jt.mapper.ItemDescMapper;
import com.jt.pojo.Item;
import com.jt.pojo.ItemDesc;
import com.jt.vo.EasyUITable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jt.mapper.ItemMapper;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

@Service
public class ItemServiceImpl implements ItemService {

	@Autowired
	private ItemMapper itemMapper;

	@Autowired
	private ItemDescMapper itemDescMapper;

	/**
	 * 利用MP方式实现分页查询
	 * @param page
	 * @param rows
	 * @return
	 */
	@Override
	public EasyUITable findItemByPage(Integer page, Integer rows) {
		//获取记录总数
		Page<Item> iPage = new Page<>();
		iPage.setSize(rows);
		iPage.setCurrent(page);
		QueryWrapper<Item> queryWrapper = new QueryWrapper<>();
		queryWrapper.orderByDesc("updated");
		IPage<Item> itemPage = itemMapper.selectPage(iPage, queryWrapper);
		//获取分页数据
		int total = (int)itemPage.getTotal();
		List<Item> itemList = itemPage.getRecords();
		return new EasyUITable(total,itemList);
	}

//	@Override
//	public EasyUITable findItemByPage(Integer page, Integer rows) {
//		//获取记录总数
//		int total = itemMapper.selectCount(null);
//		//动态获取分页的结果
//		int start = (page - 1) * rows;
//		List<Item> itemList = itemMapper.findItemByPage(start, rows);
//		return new EasyUITable(total,itemList);
//	}

	/**
	 * 两张表同时入库
	 * 问题：itemDesc的入库需要依赖于item主键信息，item的主键入库之后才有
	 * 思路：在insert之后，将数据库的信息自动封装到item对象中
	 * 解决思路：利用MP实现入库操作，之后实现回显信息
	 */
	@Override
	@Transactional
	public void saveItem(Item item, ItemDesc itemDesc) {
		//item对象主键自增，当数据入库之后查询数据库中id的最大值+1实现入库操作
		item.setStatus(1)
				.setCreated(new Date())
				.setUpdated(item.getCreated());
		itemMapper.insert(item);

		itemDesc.setItemId(item.getId())
				.setCreated(item.getCreated())
				.setUpdated(item.getCreated());
		itemDescMapper.insert(itemDesc);
	}

	@Override
	public ItemDesc findItemDescById(Long itemId) {
		return itemDescMapper.selectById(itemId);
	}

	@Override
	@Transactional //保证事务一致性
	public void updateItem(Item item, ItemDesc itemDesc) {
		Date date = new Date();
		item.setUpdated(date);
		itemMapper.updateById(item);

		itemDesc.setItemId(item.getId())
				.setUpdated(date);
		itemDescMapper.updateById(itemDesc);
	}

	@Override
	@Transactional
	public void deleteItems(Long[] ids) {
		List<Long> idList = Arrays.asList(ids);
		//itemMapper.deleteBatchIds(idList);
		itemDescMapper.deleteBatchIds(idList);

		//使用mapper xml形式实现删除操作
		itemMapper.deleteItems(ids);
	}

	/**
	 * 更新操作：修改程序的状态信息
	 */
	@Override
	public void updateStatus(Long[] ids, Integer status) {
		Item item = new Item();
		item.setStatus(status)
				.setUpdated(new Date());
		UpdateWrapper<Item> updateWrapper = new UpdateWrapper<>();
		updateWrapper.in("id", Arrays.asList(ids));
		itemMapper.update(item,updateWrapper);
	}

	@Override
	public Item findItemById(Long itemId) {
		return itemMapper.selectById(itemId);
	}
}
