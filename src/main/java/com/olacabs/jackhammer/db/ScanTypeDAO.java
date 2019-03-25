package com.olacabs.jackhammer.db;

import com.olacabs.jackhammer.models.ScanType;
import com.olacabs.jackhammer.models.mapper.ScanTypeMapper;
import org.skife.jdbi.v2.sqlobject.*;
import org.skife.jdbi.v2.sqlobject.customizers.Define;
import org.skife.jdbi.v2.sqlobject.customizers.RegisterMapper;
import org.skife.jdbi.v2.sqlobject.stringtemplate.UseStringTemplate3StatementLocator;

import java.util.List;

@UseStringTemplate3StatementLocator
@RegisterMapper(ScanTypeMapper.class)
public interface ScanTypeDAO extends CrudDAO<ScanType> {

    @SqlUpdate("insert into scanTypes(name) " +
            "values(:name)")
    @GetGeneratedKeys
    int insert(@BindBean ScanType scanType);

    @SqlQuery("select * from scanTypes")
    List<ScanType> getScanTypes();

    @SqlQuery("select * from scanTypes order by :orderBy :sortDirection LIMIT :limit OFFSET :offset")
    List<ScanType> getAll(@BindBean ScanType scanType, String sortColumn, @Define("order") String order);

    @SqlQuery("select count(*) from scanTypes")
    long totalCount();

    @SqlQuery("select * from scanTypes where id=:id")
    ScanType get(@Bind("id") long id);

    @SqlQuery("select * from scanTypes where name=:name")
    ScanType findScanTypeByName(@Bind("name") String name);

    @SqlQuery("select * from scanTypes where id=:id")
    ScanType findScanTypeById(@Bind("id") long id);

    @SqlQuery("select * from scanTypes where isStatic=true")
    ScanType getStaticScanType();

    @SqlQuery("select * from scanTypes")
    List<ScanType> getDropdownValues(@BindBean ScanType scanType);

    @SqlUpdate("update scanTypes set name=:name where id=:id ")
    void update(@BindBean ScanType scanType);

    @SqlUpdate("delete from scanTypes where id=:id")
    void delete(@Bind("id") long id);


    @SqlQuery("select * from scheduleTypes where isWordpress=true")
    ScanType getWpScanType();
}
