package com.olacabs.jackhammer.models.mapper;

import com.olacabs.jackhammer.models.FindingTag;
import org.skife.jdbi.v2.StatementContext;
import org.skife.jdbi.v2.tweak.ResultSetMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class FindingTagMapper  implements ResultSetMapper<FindingTag> {
    public FindingTag map(int index, ResultSet resultSet, StatementContext statementContext) throws SQLException {
        FindingTag findingTag = new FindingTag();
        findingTag.setFindingId(resultSet.getLong("findingId"));
        findingTag.setTagId(resultSet.getLong("tagId"));
        return findingTag;
    }
}
