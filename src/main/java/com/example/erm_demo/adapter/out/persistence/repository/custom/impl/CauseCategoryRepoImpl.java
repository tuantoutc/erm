//package com.example.erm_demo.adapter.out.persistence.repository.custom.impl;
//
//import com.example.erm_demo.adapter.out.persistence.entity.CauseCategory;
//import com.example.erm_demo.adapter.out.persistence.repository.custom.CauseCategoryRepositoryCustom;
//import jakarta.persistence.EntityManager;
//import jakarta.persistence.PersistenceContext;
//import jakarta.persistence.Query;
//import org.springframework.data.domain.Page;
//import org.springframework.data.domain.Pageable;
//import org.springframework.stereotype.Repository;
//
//import java.util.List;
//
//@Repository
//public class CauseCategoryRepoImpl implements CauseCategoryRepositoryCustom {
//
//
//    @PersistenceContext
//    private EntityManager em;
//    @Override
//    public Page<List<CauseCategory>> findByCodeOrNameAndSystem(String code, Long systemId, Pageable pageable) {
//        StringBuilder sql = new StringBuilder("SELECT distinct * from cause_categories cc");
//        StringBuilder where = new StringBuilder(" WHERE 1=1 ");
//        if (code != null && !code.equals("")) {
//            where.append(" AND (cc.code code like '%"+ code +"%' OR cc.name like '%"+code +"%') ");
//        }
//        if (systemId != null) {
//            sql.append(" inner join cause_categories_map ccm on cc.id =ccm.cause_categories_id");
//            where.append(" AND ccm.system.id = "+ systemId);
//        }
//
//        Query query = em.createNativeQuery(sql.toString(), CauseCategory.class);
//
//
//
//
//
//        return null;
//    }
//
//
//}
