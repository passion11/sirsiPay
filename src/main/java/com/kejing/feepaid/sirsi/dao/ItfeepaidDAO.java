package com.kejing.feepaid.sirsi.dao;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.kejing.feepaid.sirsi.dao.module.Tfeepaid;

public interface ItfeepaidDAO extends CrudRepository<Tfeepaid, Long> {
	Tfeepaid findByForderid(String orderid);

	@Query(value = "select * from tfeepaid where fstatus not in (0,1,2) and fcashier=?1", nativeQuery = true)
	List<Tfeepaid> queryTfeepaidNotSucc(String casherid);

	@Query(value = "select * from tfeepaid where fstatus in (1,2) and to_char(flastcommit,'yyyyMMddHH24MISS')>=?1 and to_char(flastcommit,'yyyyMMddHH24MISS')<?2", nativeQuery = true)
	List<Tfeepaid> queryTfeepaidSucc(String d1, String d2);
}
