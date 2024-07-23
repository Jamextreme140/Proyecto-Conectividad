package com.db;

import com.db.util.QueryType;
/**
 * Tool class for building queries
 */
public class QueryBuilder {
    /**
     * Builds the SQL statement
     * @param type - SQL Statement type (SELECT, INSERT, DELETE, UPDATE)
     * @param fields - Table fields (ex: [schema].[field_name] [alias])
     * @param table - Table name (ex: [schema].[table_name] [alias])
     * @param otherQuery - Custom SQL Statement
     * @return SQL Statement Builded
     */
    public static String buildQuery(QueryType type, String[] fields, String table, String otherQuery)
    {
        StringBuilder query = new StringBuilder();
        switch(type)
        {
            case SELECT -> {
                query.append("SELECT ");
                if(fields != null && fields.length > 0)
                {
                    for (int i = 0; i < fields.length; i++) {
                        query.append(fields[i]).append(", ");
                    }
                    query.deleteCharAt(query.length() - 2).append(""); // Deletes the last comma
                }
                else
                {
                    query.append("* ");
                }
                query.append("FROM ").append(table + " ").append(otherQuery == null ? "" : otherQuery);
            }
            case INSERT -> {
                query.append("INSERT INTO " + table + " ");
                if((fields != null && fields.length > 0) && (otherQuery != null && !otherQuery.isBlank()))
                {
                    query.append("(");
                    for (int i = 0; i < fields.length; i++) {
                        query.append(fields[i]).append(", ");
                    }
                    query.deleteCharAt(query.length() - 2).append(") ");
                    query.append("VALUES ").append(otherQuery);
                }
                else
                {
                    System.out.println("ERROR: No fields or values specified for UPDATE");
                    return null;
                }
            }
            case DELETE -> {
                query.append("DELETE FROM " + table + " ");
                if(otherQuery != null && otherQuery.contains("WHERE"))
                {
                    query.append(otherQuery);
                }
                else
                    System.out.println("WARNING: No WHERE clause on DELETE");
            }
            case UPDATE -> {
                query.append("UPDATE " + table + " ");
                if(fields != null && fields.length > 0)
                {
                    query.append("SET ");
                    for (int i = 0; i < fields.length; i++) {
                        query.append(fields[i]).append(", ");
                    }
                    query.deleteCharAt(query.length() - 2).append(" ");

                    if (!otherQuery.isBlank() || otherQuery.contains("WHERE")) 
                        query.append(otherQuery);
                    else
                        System.out.println("WARNING: No WHERE clause on UPDATE");
                }
                else 
                {
                    System.out.println("ERROR: No fields specified for UPDATE");
                    return null;
                }
            }
        }
        return query.toString();
    }
}