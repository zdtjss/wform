package com.nway.platform.wform.commons;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import javax.sql.DataSource;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

@Component
public class DatabaseInitListener implements ApplicationListener<ContextRefreshedEvent> {

	private static final Logger log = LoggerFactory.getLogger(DatabaseInitListener.class);

	@Override
	public void onApplicationEvent(ContextRefreshedEvent event) {

		String sqlStatement = null;
		Connection connection = null;
		
		try {
			connection = event.getApplicationContext().getBean(DataSource.class).getConnection();
			
			connection.setAutoCommit(false);
			
			String ddlStatements = IOUtils.toString(
					DatabaseInitListener.class.getResourceAsStream("/sql/wform-hsqldb.sql"), "UTF8");

			BufferedReader reader = new BufferedReader(new StringReader(ddlStatements));

			String line = readNextTrimmedLine(reader);
			while (line != null) {
				if (line.startsWith("# ")) {
					log.debug(line.substring(2));

				} else if (line.startsWith("-- ")) {
					log.debug(line.substring(3));

				} else if (line.length() > 0) {

					if (line.endsWith(";")) {

						sqlStatement = addSqlStatementPiece(sqlStatement, line.substring(0, line.length() - 1));

						Statement jdbcStatement = connection.createStatement();
						try {
							log.debug("SQL: {}", sqlStatement);
							jdbcStatement.execute(sqlStatement);
							jdbcStatement.close();
						} catch (Exception e) {
							log.error("自动初始化数据库失败", e);
						} finally {
							sqlStatement = null;
						}
					} else {
						sqlStatement = addSqlStatementPiece(sqlStatement, line);
					}
				}

				line = readNextTrimmedLine(reader);
			}
		} catch (Exception e) {

			log.error("自动初始化数据库失败", e);
		}
		finally {

			if (connection != null) {
				
				try {
					connection.commit();
					connection.close();
				} catch (SQLException e) {
					log.warn("关于连接失败");
				}
			}
		}

		log.debug("wform db initialization completed");
	}

	protected String addSqlStatementPiece(String sqlStatement, String line) {
		if (sqlStatement == null) {
			return line;
		}
		return sqlStatement + " \n" + line;
	}

	protected String readNextTrimmedLine(BufferedReader reader) throws IOException {
		String line = reader.readLine();
		if (line != null) {
			line = line.trim();
		}
		return line;
	}

}
