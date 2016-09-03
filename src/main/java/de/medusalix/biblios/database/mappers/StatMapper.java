/*
 * Copyright (C) 2016 Medusalix
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package de.medusalix.biblios.database.mappers;

import de.medusalix.biblios.database.objects.Stat;
import org.skife.jdbi.v2.BeanMapper;
import org.skife.jdbi.v2.StatementContext;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

public class StatMapper extends BeanMapper<Stat>
{
    public StatMapper()
    {
        super(Stat.class);
    }

    @Override
    public Stat map(int row, ResultSet rs, StatementContext ctx) throws SQLException
    {
        Stat stat = super.map(row, rs, ctx);

        ResultSetMetaData metaData = rs.getMetaData();

        for (int i = 1; i <= metaData.getColumnCount(); i++)
        {
            String columnName = metaData.getColumnName(i);

            if (columnName.equals("Title"))
            {
                stat.setBookTitle(rs.getString(i));
            }
        }

        return stat;
    }
}
