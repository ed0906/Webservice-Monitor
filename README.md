Webservice-Monitor
==================

Simple webservice monitor with a Java &amp; MySQL back-end

Currently the application expects to find a MySQL database with the following structure:
	Tables:
		- WEBSERVICE
			Columns:
				- NAME (varchar(255))
				- URL (varchar(255))
		- WEBSERVICE_METRICS
			Columns:
				- NAME (varchar(255))
				- STATUS (int(3)))
				- RESPONSE_TIME (int(14))
				- DATE (TIMESTAMP)
		