package com.olacabs.jackhammer.db;

import com.olacabs.jackhammer.models.Language;
import com.olacabs.jackhammer.models.mapper.LanguageMapper;

import java.util.List;

import org.skife.jdbi.v2.sqlobject.*;
import org.skife.jdbi.v2.sqlobject.customizers.Define;
import org.skife.jdbi.v2.sqlobject.customizers.RegisterMapper;
import org.skife.jdbi.v2.sqlobject.stringtemplate.UseStringTemplate3StatementLocator;

@UseStringTemplate3StatementLocator
@RegisterMapper(LanguageMapper.class)
public interface LanguageDAO {

    @SqlQuery("select * from languages order by <sortColumn> <order>  LIMIT :limit OFFSET :offset")
    List<Language> getAll(@BindBean Language language, @Define("sortColumn") String sortColumn, @Define("order") String order);

    @SqlUpdate("insert into languages(name,fileExtension) " +
            "values(:name,:fileExtension)")
    @GetGeneratedKeys
    int insert(@BindBean Language language);


    @SqlQuery("select * from languages where name like concat('%', :searchTerm,'%') order by <sortColumn> <order> LIMIT :limit OFFSET :offset")
    List<Language> getSearchResults(@BindBean Language language, @Define("sortColumn") String sortColumn,@Define("order") String order);

    @SqlQuery("select count(*) from languages where name like concat('%', :searchTerm,'%')")
    long totalSearchCount(@BindBean Language language);

    @SqlQuery("select count(*) from languages")
    long totalCount(@BindBean Language language);

    @SqlQuery("select * from languages where id=:id")
    Language get(@Bind("id") long id);

    @SqlQuery("select * from languages where name=:name")
    Language update(@BindBean Language language);

    @SqlQuery("select * from languages where target=:name")
    Language findLanguageByName(@Bind("name") String name);

    @SqlQuery("select * from languages")
    List<Language> getLanguages();

    @SqlUpdate("delete from languages where id=:id")
    void delete(@Bind("id") long id);

    @SqlQuery("select * from languages")
    List<Language>  getDropdownValues();
}
