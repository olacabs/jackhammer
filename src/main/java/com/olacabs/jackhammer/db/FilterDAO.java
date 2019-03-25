package com.olacabs.jackhammer.db;

import com.olacabs.jackhammer.models.Filter;
import com.olacabs.jackhammer.models.Finding;
import com.olacabs.jackhammer.models.VulnerableType;
import com.olacabs.jackhammer.models.mapper.FindingMapper;
import com.olacabs.jackhammer.models.mapper.VulnerableTypeMapper;
import org.skife.jdbi.v2.sqlobject.BindBean;
import org.skife.jdbi.v2.sqlobject.SqlQuery;
import org.skife.jdbi.v2.sqlobject.customizers.Define;
import org.skife.jdbi.v2.sqlobject.customizers.RegisterMapper;
import org.skife.jdbi.v2.sqlobject.stringtemplate.UseStringTemplate3StatementLocator;

import java.util.List;

@UseStringTemplate3StatementLocator
public interface FilterDAO {
    @SqlQuery("select distinct(name) from findings where scanTypeId=:scanTypeId and ownerTypeId=:ownerTypeId and isDeleted=false")
    @RegisterMapper(VulnerableTypeMapper.class)
    List<VulnerableType> getVulnerableTypes(@BindBean Filter filter);


    @SqlQuery("select * from findings  where <where> order by <sortColumn> <order> LIMIT :limit OFFSET :offset")
    @RegisterMapper(FindingMapper.class)
    List<Finding> getFilterResults(@BindBean Filter filter,@Define("where") String where,@Define("sortColumn") String sortColumn, @Define("order") String order);

    @SqlQuery("select count(*) from findings  where <where>")
    long totalFilterCount(@BindBean Filter filter,@Define("where") String where);


    @SqlQuery("select * from findings  where <where> " +
            " and (name like concat('%', :searchTerm,'%') " +
            "or severity like concat('%', :searchTerm,'%') " +
            "or toolName like concat('%', :searchTerm,'%'))  " +
            "order by <sortColumn> <order> LIMIT :limit OFFSET :offset")
    @RegisterMapper(FindingMapper.class)
    List<Finding> getFilterSearchResults(@BindBean Filter filter, @Define("where") String where,@Define("sortColumn") String sortColumn, @Define("order") String order);

    @SqlQuery("select count(*) from findings  where <where> " +
            " and (name like concat('%', :searchTerm,'%') " +
            "or severity like concat('%', :searchTerm,'%') " +
            "or toolName like concat('%', :searchTerm,'%'))  ")
    long totalFilterSearchCount(@BindBean Filter filter,@Define("where") String where);
}
