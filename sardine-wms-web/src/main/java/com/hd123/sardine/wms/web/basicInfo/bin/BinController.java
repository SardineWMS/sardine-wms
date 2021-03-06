/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2016，所有权利保留。
 * 
 * 项目名： sardine-wms-web
 * 文件名： BinController.java
 * 模块说明：    
 * 修改历史：
 * 2016年12月12日 - zhangsai - 创建。
 */
package com.hd123.sardine.wms.web.basicInfo.bin;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.hd123.rumba.commons.lang.StringUtil;
import com.hd123.sardine.wms.api.basicInfo.bin.Bin;
import com.hd123.sardine.wms.api.basicInfo.bin.BinInfo;
import com.hd123.sardine.wms.api.basicInfo.bin.BinService;
import com.hd123.sardine.wms.api.basicInfo.bin.BinState;
import com.hd123.sardine.wms.api.basicInfo.bin.BinUsage;
import com.hd123.sardine.wms.api.basicInfo.bin.Path;
import com.hd123.sardine.wms.api.basicInfo.bin.Wrh;
import com.hd123.sardine.wms.api.basicInfo.bin.Zone;
import com.hd123.sardine.wms.api.basicInfo.bintype.BinType;
import com.hd123.sardine.wms.api.basicInfo.bintype.BinTypeService;
import com.hd123.sardine.wms.api.stock.Stock;
import com.hd123.sardine.wms.api.stock.StockComponent;
import com.hd123.sardine.wms.api.stock.StockFilter;
import com.hd123.sardine.wms.api.stock.StockService;
import com.hd123.sardine.wms.common.entity.UCN;
import com.hd123.sardine.wms.common.exception.NotLoginInfoException;
import com.hd123.sardine.wms.common.http.ErrorRespObject;
import com.hd123.sardine.wms.common.http.RespObject;
import com.hd123.sardine.wms.common.http.RespStatus;
import com.hd123.sardine.wms.common.query.OrderDir;
import com.hd123.sardine.wms.common.query.PageQueryDefinition;
import com.hd123.sardine.wms.common.query.PageQueryResult;
import com.hd123.sardine.wms.common.utils.ApplicationContextUtil;
import com.hd123.sardine.wms.web.base.BaseController;

/**
 * 货位控制层
 * 
 * @author zhangsai
 *
 */
@RestController
@RequestMapping("/basicinfo/bin")
public class BinController extends BaseController {
	@Autowired
	private BinService binService;

	@Autowired
	private BinTypeService binTypeService;

	@Autowired
	private StockService stockService;

	@RequestMapping(value = "/query", method = RequestMethod.GET)
	public @ResponseBody RespObject query(@RequestParam(value = "token", required = true) String token,
			@RequestParam(value = "page", required = false, defaultValue = "1") int page,
			@RequestParam(value = "pageSize", required = false, defaultValue = "50") int pageSize,
			@RequestParam(value = "sort", required = false) String sort,
			@RequestParam(value = "wrhUuid", required = false) String wrhUuid,
			@RequestParam(value = "zoneUuid", required = false) String zoneUuid,
			@RequestParam(value = "pathUuid", required = false) String pathUuid,
			@RequestParam(value = "shelfUuid", required = false) String shelfUuid,
			@RequestParam(value = "code", required = false) String code,
			@RequestParam(value = "state", required = false) String state,
			@RequestParam(value = "usage", required = false) String usage,
			@RequestParam(value = "order", required = false, defaultValue = "asc") String sortDirection) {
		RespObject resp = new RespObject();
		try {
			PageQueryDefinition definition = new PageQueryDefinition();
			definition.setPage(page);
			definition.setPageSize(pageSize);
			definition.setSortField(sort);
			definition.setOrderDir(OrderDir.valueOf(sortDirection));
			definition.put(BinService.QUERY_WRH_FIELD, wrhUuid);
			definition.put(BinService.QUERY_ZONE_FIELD, zoneUuid);
			definition.put(BinService.QUERY_PATH_FIELD, pathUuid);
			definition.put(BinService.QUERY_SHELF_FIELD, shelfUuid);
			definition.put(BinService.QUERY_CODE_FIELD, code);
			definition.put(BinService.QUERY_STATE_FIELD,
					StringUtil.isNullOrBlank(state) ? null : BinState.valueOf(state));
			definition.put(BinService.QUERY_USAGE_FIELD,
					StringUtil.isNullOrBlank(usage) ? null : BinUsage.valueOf(usage));

			PageQueryResult<Bin> result = binService.queryBin(definition);
			List<BinInfo> infos = binService.queryTreeData();
			BinQueryResult resultData = new BinQueryResult();
			resultData.setPageData(result);
			resultData.setTreeData(infos);
			resp.setObj(resultData);
			resp.setStatus(RespStatus.HTTP_STATUS_SUCCESS);
		} catch (Exception e) {
			return new ErrorRespObject("分页查询失败：" + e.getMessage());
		}
		return resp;
	}
	
	@RequestMapping(value = "/queryByPage", method = RequestMethod.GET)
	public @ResponseBody RespObject queryByPage(@RequestParam(value = "token", required = true) String token,
			@RequestParam(value = "page", required = false, defaultValue = "1") int page,
			@RequestParam(value = "pageSize", required = false, defaultValue = "20") int pageSize,
			@RequestParam(value = "sort", required = false) String sort,
			@RequestParam(value = "code", required = false) String code,
			@RequestParam(value = "state", required = false) String state,
			@RequestParam(value = "usage", required = false) String usage,
			@RequestParam(value = "order", required = false, defaultValue = "asc") String sortDirection) {
		RespObject resp = new RespObject();
		try {
			PageQueryDefinition definition = new PageQueryDefinition();
			definition.setPage(page);
			definition.setPageSize(pageSize);
			definition.setSortField(sort);
			definition.setOrderDir(OrderDir.valueOf(sortDirection));
			definition.put(BinService.QUERY_CODE_FIELD, code);
			definition.put(BinService.QUERY_STATE_FIELD,
					StringUtil.isNullOrBlank(state) ? null : BinState.valueOf(state));
			definition.put(BinService.QUERY_USAGE_FIELD,
					StringUtil.isNullOrBlank(usage) ? null : BinUsage.valueOf(usage));

			PageQueryResult<Bin> result = binService.queryBin(definition);
			resp.setObj(result);
			resp.setStatus(RespStatus.HTTP_STATUS_SUCCESS);
		} catch (Exception e) {
			return new ErrorRespObject("分页查询失败：" + e.getMessage());
		}
		return resp;
	}

	@RequestMapping(value = "/queryWrhs", method = RequestMethod.GET)
	public @ResponseBody RespObject queryWrhs(@RequestParam(value = "token", required = true) String token) {
		RespObject resp = new RespObject();
		try {
			List<Wrh> result = binService.queryWrhs();
			resp.setObj(result);
			resp.setStatus(RespStatus.HTTP_STATUS_SUCCESS);
		} catch (Exception e) {
			return new ErrorRespObject("查询仓位信息失败：" + e.getMessage());
		}
		return resp;
	}

	@RequestMapping(value = "/queryZones", method = RequestMethod.GET)
	public @ResponseBody RespObject queryZones(@RequestParam(value = "token", required = true) String token) {
		RespObject resp = new RespObject();
		try {
			List<Zone> result = binService.queryZones();
			resp.setObj(result);
			resp.setStatus(RespStatus.HTTP_STATUS_SUCCESS);
		} catch (Exception e) {
			return new ErrorRespObject("查询货区信息失败：" + e.getMessage());
		}
		return resp;
	}

	@RequestMapping(value = "/queryBinTypes", method = RequestMethod.GET)
	public @ResponseBody RespObject queryBinTypes(@RequestParam(value = "token", required = true) String token) {
		RespObject resp = new RespObject();
		try {
			PageQueryDefinition definition = new PageQueryDefinition();
			definition.setPage(1);
			definition.setPageSize(1000);
			PageQueryResult<BinType> binTypes = binTypeService.query(definition);
			resp.setObj(binTypes.getRecords());
			resp.setStatus(RespStatus.HTTP_STATUS_SUCCESS);
		} catch (Exception e) {
			return new ErrorRespObject("查询货位类型失败：" + e.getMessage());
		}
		return resp;
	}

	@RequestMapping(value = "/insertWrh", method = RequestMethod.PUT)
	public @ResponseBody RespObject insertWrh(@RequestParam(value = "code", required = true) String code,
			@RequestParam(value = "token", required = true) String token,
			@RequestParam(value = "name", required = true) String name,
			@RequestParam(value = "note", required = false) String note) {
		RespObject resp = new RespObject();
		try {
			Wrh wrh = new Wrh();
			wrh.setCode(code);
			wrh.setName(name);
			wrh.setNote(note);
			wrh.setCompanyUuid(ApplicationContextUtil.getCompanyUuid());
			binService.insertWrh(wrh);
			resp.setStatus(RespStatus.HTTP_STATUS_SUCCESS);
		} catch (Exception e) {
			return new ErrorRespObject("保存仓位失败：" + e.getMessage());
		}
		return resp;
	}

	@RequestMapping(value = "/insertZone", method = RequestMethod.PUT)
	public @ResponseBody RespObject insertZone(@RequestParam(value = "code", required = true) String code,
			@RequestParam(value = "token", required = true) String token,
			@RequestParam(value = "name", required = true) String name,
			@RequestParam(value = "wrhUuid", required = true) String wrhUuid,
			@RequestParam(value = "note", required = false) String note) {
		RespObject resp = new RespObject();
		try {
			Zone zone = new Zone();
			zone.setCode(code);
			zone.setName(name);
			zone.setNote(note);
			zone.setWrh(new UCN(wrhUuid, null, null));
			zone.setCompanyUuid(ApplicationContextUtil.getCompanyUuid());
			binService.insertZone(zone);
			resp.setStatus(RespStatus.HTTP_STATUS_SUCCESS);
		} catch (Exception e) {
			e.printStackTrace();
			return new ErrorRespObject("保存货区失败：" + e.getMessage());
		}
		return resp;
	}

	@RequestMapping(value = "/insertPath", method = RequestMethod.PUT)
	public @ResponseBody RespObject insertPath(@RequestParam(value = "token", required = true) String token,
			@RequestParam(value = "zoneUuid", required = true) String zoneUuid) {
		RespObject resp = new RespObject();
		try {
			Path path = new Path();
			path.setZoneUuid(zoneUuid);
			path.setCompanyUuid(ApplicationContextUtil.getCompanyUuid());
			binService.insertPath(path);
			resp.setStatus(RespStatus.HTTP_STATUS_SUCCESS);
		} catch (Exception e) {
			return new ErrorRespObject("保存货道失败：" + e.getMessage());
		}
		return resp;
	}

	@RequestMapping(value = "/insertShelf", method = RequestMethod.PUT)
	public @ResponseBody RespObject insertShelf(@RequestParam(value = "token", required = true) String token,
			@RequestParam(value = "pathCode", required = true) String pathCode) {
		RespObject resp = new RespObject();
		try {
			binService.insertShelf(pathCode);
			resp.setStatus(RespStatus.HTTP_STATUS_SUCCESS);
		} catch (Exception e) {
			return new ErrorRespObject("保存货架失败：" + e.getMessage());
		}
		return resp;
	}

	@RequestMapping(value = "/insertBin", method = RequestMethod.PUT)
	public @ResponseBody RespObject insertBin(@RequestParam(value = "token", required = true) String token,
			@RequestParam(value = "code", required = true) String code,
			@RequestParam(value = "binTypeUuid", required = true) String binTypeUuid,
			@RequestParam(value = "binUsage", required = true) String binUsage) {
		RespObject resp = new RespObject();
		try {
			Bin bin = new Bin();
			bin.setCode(code);
			bin.setUsage(BinUsage.valueOf(binUsage));
			bin.setBinType(new UCN(binTypeUuid, null, null));
			bin.setCompanyUuid(ApplicationContextUtil.getCompanyUuid());
			binService.insertBin(bin);
			resp.setStatus(RespStatus.HTTP_STATUS_SUCCESS);
		} catch (Exception e) {
			return new ErrorRespObject("保存货位失败：" + e.getMessage());
		}
		return resp;
	}

	@RequestMapping(value = "/delete", method = RequestMethod.DELETE)
	public @ResponseBody RespObject delete(@RequestParam(value = "uuid", required = true) String uuid,
			@RequestParam(value = "version", required = false) long version,
			@RequestParam(value = "token", required = true) String token) {
		RespObject resp = new RespObject();
		try {
			binService.remove(uuid, version);
			resp.setStatus(RespStatus.HTTP_STATUS_SUCCESS);
		} catch (Exception e) {
			return new ErrorRespObject("删除货位失败：" + e.getMessage());
		}
		return resp;
	}

	@RequestMapping(value = "/getBinByCode", method = RequestMethod.GET)
	public @ResponseBody RespObject getBinByCode(@RequestParam(value = "bincode", required = true) String bincode,
			@RequestParam(value = "token", required = true) String token) {
		RespObject resp = new RespObject();
		try {
			Bin bin = binService.getBinByCode(bincode);
			resp.setObj(bin);
			resp.setStatus(RespStatus.HTTP_STATUS_SUCCESS);
		} catch (NotLoginInfoException e) {
			return new ErrorRespObject("登陆信息为空，请重新登陆：" + e.getMessage());
		} catch (Exception e) {
			return new ErrorRespObject("根据代码查询货位失败：" + e.getMessage());
		}
		return resp;
	}

	@RequestMapping(value = "/getbinbywrhandusage", method = RequestMethod.GET)
	public @ResponseBody RespObject getBinByWrhAndUsage(
			@RequestParam(value = "wrhUuid", required = false) String wrhUuid,
			@RequestParam(value = "binUsage", required = true) String binUsage) {
		RespObject resp = new RespObject();
		try {
			Bin bin = binService.getBinByWrhAndUsage(wrhUuid, BinUsage.valueOf(binUsage));
			if (Objects.isNull(bin) == false)
				resp.setObj(bin.getCode());
			resp.setStatus(RespStatus.HTTP_STATUS_SUCCESS);
		} catch (Exception e) {
			return new ErrorRespObject("获取仓位下指定货位用途的货位失败：" + e.getMessage());
		}
		return resp;
	}

	@RequestMapping(value = "/closeWrh", method = RequestMethod.PUT)
	public @ResponseBody RespObject closeWrh(@RequestParam(value = "wrhUuid", required = false) String uuid,
			@RequestParam(value = "binScope", required = false) String binScope,
			@RequestParam(value = "token", required = false) String token) {
		RespObject resp = new RespObject();
		try {
			binService.closeWrh(uuid, binScope);
			resp.setStatus(RespStatus.HTTP_STATUS_SUCCESS);
		} catch (Exception e) {
			return new ErrorRespObject("封仓失败：" + e.getMessage());
		}
		return resp;
	}

	@RequestMapping(value = "/releaseWrh", method = RequestMethod.PUT)
	public @ResponseBody RespObject releaseWrh(@RequestParam(value = "wrhUuid", required = false) String uuid,
			@RequestParam(value = "binScope", required = false) String binScope,
			@RequestParam(value = "token", required = false) String token) {
		RespObject resp = new RespObject();
		try {
			binService.releaseWrh(uuid, binScope);
			resp.setStatus(RespStatus.HTTP_STATUS_SUCCESS);
		} catch (Exception e) {
			return new ErrorRespObject("解仓失败：" + e.getMessage());
		}
		return resp;
	}

	@RequestMapping(value = "/queryBinStockInfo", method = RequestMethod.GET)
	public @ResponseBody RespObject queryBinStockInfo(@RequestParam(value = "binCode", required = false) String binCode,
			@RequestParam(value = "token", required = false) String token) {
		RespObject resp = new RespObject();
		try {
			StockFilter filter = new StockFilter();
			filter.setBinCode(binCode);
			filter.setPageSize(0);
			List<Stock> stocks = stockService.query(filter);
			List<StockComponent> component = new ArrayList<StockComponent>();
			for (Stock stock : stocks) {
				component.add(stock.getStockComponent());
			}
			resp.setObj(component);
			resp.setStatus(RespStatus.HTTP_STATUS_SUCCESS);
		} catch (NotLoginInfoException e) {
			return new ErrorRespObject("登录信息为空，请重新登录：" + e.getMessage());
		} catch (Exception e) {
			return new ErrorRespObject("查询货位库存信息失败：" + e.getMessage());
		}
		return resp;
	}

}